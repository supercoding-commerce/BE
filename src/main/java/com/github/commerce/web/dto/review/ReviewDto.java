package com.github.commerce.web.dto.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.Review;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long productId;
    private String productName;
    private String options;
    private Long reviewId;
    private String author;
    private String title;
    private String content;
    private Integer starPoint;
    private String imageUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;



    public static ReviewDto fromEntity(Review review){
        Product product = review.getProducts();
        return ReviewDto.builder()
                .reviewId(review.getId())
                .author(review.getAuthor())
                .productId(product.getId())
                .productName(product.getName())
                .options(product.getOptions())
                .starPoint(Integer.valueOf(review.getStarPoint()))
                .title(review.getTitle())
                .content(review.getContent())
                .imageUrl(review.getImageUrl())
                .createdAt(review.getCreatedAt())
                .build();
    }
}