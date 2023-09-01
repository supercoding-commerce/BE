package com.github.commerce.web.controller.review;

import com.github.commerce.service.review.ReviewService;
import com.github.commerce.web.dto.review.GetReviewDto;
import com.github.commerce.web.dto.review.PostReviewDto;
import com.github.commerce.web.dto.review.ReviewDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/review")
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 등록
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<PostReviewDto.Response> createReview(
            @RequestBody PostReviewDto.Request request
            //@RequestPart List<MultipartFile> multipartFile
    ){
        Long userId = 1L;
        return ResponseEntity.ok(
                PostReviewDto.Response.from(
                        reviewService.createReview(request, userId)
                )
        );
    }

    /**
     * 상품 리뷰 조회
     * @param productId
     * @param cursorId
     * @return
     */
    @GetMapping("/{productId}")
    public ResponseEntity<List<ReviewDto>> get(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") Integer cursorId
    ){
        return ResponseEntity.ok(
                GetReviewDto.Response.fromRawResult(
                        reviewService.getReviews(productId, cursorId)
                )
                //reviewService.getReviews(productId, cursorId)
        );
    }

    /**
     * 리뷰 삭제
     * @param reviewId
     * @return
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> delete(
            @PathVariable Long reviewId
    ){
        Long userId = 1L;
        return ResponseEntity.ok(
                reviewService.deleteReview(reviewId, userId)
        );
    }
}
