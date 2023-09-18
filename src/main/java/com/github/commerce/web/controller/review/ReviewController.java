package com.github.commerce.web.controller.review;

import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.review.ReviewService;
import com.github.commerce.web.dto.review.PostReviewDto;
import com.github.commerce.web.dto.review.ReviewDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = PostReviewDto.Response.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReviewDto> createReview(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(name = "request") String request,//JSON.stringify()
            @RequestParam(required = false) MultipartFile multipartFile
    ){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(
                reviewService.createReview(request, userId, multipartFile)

        );
    }



    /**
     *
     * 리뷰 삭제
     * @param reviewId
     * @return
     */
    @ApiOperation(value = "리뷰삭제, 로그인 필요")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
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
