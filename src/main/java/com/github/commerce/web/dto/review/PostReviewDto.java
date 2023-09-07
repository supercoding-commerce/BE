package com.github.commerce.web.dto.review;

import lombok.*;

public class PostReviewDto {
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReviewRequest {
        private Long productId;
        private String title;
        private String content;
        private Short starPoint;

    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private Long reviewId;
        private Long productId;
        private String author;
        private String title;
        private String content;
        private Integer starPoint;


        public static PostReviewDto.Response from(ReviewDto reviewDto) {
            return Response.builder()
                    .author(reviewDto.getAuthor())
                    .reviewId(reviewDto.getReviewId())
                    .productId(reviewDto.getProductId())
                    .title(reviewDto.getTitle())
                    .content(reviewDto.getContent())
                    .starPoint(reviewDto.getStarPoint())
                    .build();
        }

    }
}
