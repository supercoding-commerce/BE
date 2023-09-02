package com.github.commerce.service.review;

import com.github.commerce.entity.*;
import com.github.commerce.repository.payment.PaymentHistoryRepository;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.review.ReviewRepository;
import com.github.commerce.repository.user.UserInfoRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.review.exception.ReviewErrorCode;
import com.github.commerce.service.review.exception.ReviewException;
import com.github.commerce.web.dto.review.GetReviewDto;
import com.github.commerce.web.dto.review.PostReviewDto;
import com.github.commerce.web.dto.review.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewDto createReview(PostReviewDto.Request request, Long userId) {

        Long productId = request.getProductId();
        Long paymentHistoryId = request.getPaymentHistoryId();
        User validatedUser = validateUser(userId);
        PaymentHistory validatedPayment = validatePayment(paymentHistoryId, productId);
        UsersInfo validatedUsersInfo = validateUserInfo(userId);
        Product validatedProduct = validateProduct(productId);
        if (existReview(paymentHistoryId, productId)) {
            throw new ReviewException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
        }
        if (validatedPayment.getPoint() == null) {
            throw new ReviewException(ReviewErrorCode.PAYMENT_POINT_NULL);
        }

        ReviewDto reviewDto = ReviewDto.fromEntity(
                reviewRepository.save(
                    Review.builder()
                        .paymentHistories(validatedPayment)
                        .users(validatedUser)
                        .products(validatedProduct)
                        .author(validatedUsersInfo.getNickname())
                        .title(request.getTitle())
                        .content(request.getContent())
                        .starPoint(request.getStarPoint())
                        .isDeleted(false)
                        .createdAt(LocalDateTime.now())
                        .build()
                )
        );

        //포인트 적립 결제액 2%
        BigDecimal point = validatedPayment.getPoint();
        Long paymentAmount = validatedPayment.getPaymentAmount();
        BigDecimal additionalPoints = new BigDecimal(paymentAmount)
                .multiply(new BigDecimal("0.02"));
        BigDecimal modifiedPoint = point.add(additionalPoints);
        validatedPayment.setPoint(modifiedPoint);
        paymentHistoryRepository.save(validatedPayment);

        return reviewDto;
    }

    @Transactional(readOnly = true)
    public List<Object[]> getReviews(Long productId, Integer cursorId){

        validateProduct(productId);

        return reviewRepository.findReviewsByProductId(
                productId, false, cursorId, PageRequest.of(0, 10));

    }


    @Transactional
    public String deleteReview(Long reviewId, Long userId){

        validateUser(userId);
        Review validatedReview = validateReviewAuthor(reviewId, userId);

        validatedReview.setIsDeleted(true);
        reviewRepository.save(
                validatedReview
        );

        return validatedReview.getProducts().getName() + "에 대한 리뷰가 삭제되었습니다.";
    }



    private User validateUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.USER_NOT_FOUND));
    }

    private UsersInfo validateUserInfo(Long userId){
        return userInfoRepository.findByUsersId(userId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.USER_INFO_NOT_FOUD));
    }

    private PaymentHistory validatePayment(Long paymentHistoryId, Long productId){
        return paymentHistoryRepository.findByIdAndProductId(paymentHistoryId, productId)
                .orElseThrow(()->new ReviewException(ReviewErrorCode.REVIEW_PERMISSION_DENIED));
    }

    private Product validateProduct(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(
                        () -> new ReviewException(ReviewErrorCode.THIS_PRODUCT_DOES_NOT_EXIST));
    }

    private boolean existReview(Long paymentHistoryId, Long productId){
        return reviewRepository.existsByPaymentHistoriesIdAndProductsId(paymentHistoryId, productId);
    }

    private Review validateReviewAuthor(Long reviewId, Long userId){
        Review review = reviewRepository.findByIdAndUsersIdAndIsDeleted(reviewId, userId, false);
        if (review == null) {
            throw new ReviewException(ReviewErrorCode.NO_PERMISSION_TO_DELETE);
        }
        return review;
    }


}
