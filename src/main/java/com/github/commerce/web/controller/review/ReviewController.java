package com.github.commerce.web.controller.review;

import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.review.ReviewService;
import com.github.commerce.web.dto.review.GetReviewDto;
import com.github.commerce.web.dto.review.PostReviewDto;
import com.github.commerce.web.dto.review.ReviewDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "물품리뷰 API")
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
    @ApiOperation(value = "리뷰 등록, 로그인필요")
    @PostMapping
    public ResponseEntity<PostReviewDto.Response> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody PostReviewDto.Request request
            //@RequestPart List<MultipartFile> multipartFile
    ){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(
                PostReviewDto.Response.from(
                        reviewService.createReview(request, userId)
                )
        );
    }

    /**
     * 로그인 필요 없음
     * 상품 리뷰 조회
     * @param productId
     * @param cursorId
     * @return
     */
    @ApiOperation(value = "개별상품 리뷰 전체조회, 로그인 필요없음, cursorId는 없어도 됩니다")
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
     *
     * 리뷰 삭제
     * @param reviewId
     * @return
     */
    @ApiOperation(value = "리뷰삭제, 로그인 필요")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> delete(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reviewId
    ){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(
                reviewService.deleteReview(reviewId, userId)
        );
    }
}
