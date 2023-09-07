package com.github.commerce.service.review;

import com.github.commerce.entity.*;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.repository.payment.PayMoneyRepository;
import com.github.commerce.repository.payment.PaymentHistoryRepository;
import com.github.commerce.repository.payment.PaymentRepository;
import com.github.commerce.repository.payment.PointHistoryRepository;
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
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PayMoneyRepository payMoneyRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public ReviewDto createReview(PostReviewDto.Request request, Long userId) {

        Long productId = request.getProductId();
        User validatedUser = validateUser(userId);
        Order validatedPaidOrder = validatePaidOrder(userId, productId);
        UsersInfo validatedUsersInfo = validateUserInfo(userId);
        Product validatedProduct = validateProduct(productId);
        PayMoney validatedPay = validatePayMoneyForReviewPoint(userId);
        if (existReview(validatedPaidOrder.getId(), productId)) {
            throw new ReviewException(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
        }
        ReviewDto reviewDto = ReviewDto.fromEntity(
                reviewRepository.save(
                    Review.builder()
                        .orders(validatedPaidOrder)
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
        Long point = validatedPay.getPointBalance();
        Long paidPrice = validatedPaidOrder.getTotalPrice();
        Long additionalPoints = Math.round(paidPrice * 0.02);
        //적립 포인트 기록
        pointHistoryRepository.save(
                PointHistory.builder()
                        .payMoney(validatedPay)
                        .earnedPoint(additionalPoints)
                        .createAt(LocalDateTime.now())
                        .usedPoint(0L)
                        .status(1)
                        .build()
        );

        //
        Long modifiedPoint = point + additionalPoints;
        validatedPay.setPointBalance(modifiedPoint);
        payMoneyRepository.save(validatedPay);

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

    private Order validatePaidOrder(Long userId, Long productId){
        return orderRepository.validatePaidOrderByUsersIdAndProductsId(userId, productId)
                .orElseThrow(()->new ReviewException(ReviewErrorCode.REVIEW_PERMISSION_DENIED));
    }

    private Product validateProduct(Long productId){
        return productRepository.findById(productId)
                .orElseThrow(
                        () -> new ReviewException(ReviewErrorCode.THIS_PRODUCT_DOES_NOT_EXIST));
    }

    private boolean existReview(Long orderId, Long productId){
        return reviewRepository.existsByOrdersIdAndProductsId(orderId, productId);
    }

    private Review validateReviewAuthor(Long reviewId, Long userId){
        Review review = reviewRepository.findByIdAndUsersIdAndIsDeleted(reviewId, userId, false);
        if (review == null) {
            throw new ReviewException(ReviewErrorCode.NO_PERMISSION_TO_DELETE);
        }
        return review;
    }

    private PayMoney validatePayMoneyForReviewPoint(Long userId) {
        return payMoneyRepository.findByUsersId(userId).orElseThrow(()->new ReviewException(ReviewErrorCode.PAYMONEY_NOT_FOUD));
    }
}
