package com.github.commerce.web.dto.review;

import com.github.commerce.web.dto.cart.CartDto;
import com.github.commerce.web.dto.cart.GetCartDto;
import lombok.*;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;
import java.util.stream.Collectors;

public class GetReviewDto {
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        //private List<ReviewDto> content;

        private Long paymentHistoryId;
        private Long reviewId;
        private Long productId;
        private String author;
        private String title;
        private String content;
        private Integer starPoint;
        private LocalDateTime createdAt;

//        public static GetReviewDto.Response from(ReviewDto reviewDto) {
//            return Response.builder()
//                    .paymentHistoryId(reviewDto.getPaymentHistoryId())
//                    .author(reviewDto.getAuthor())
//                    .reviewId(reviewDto.getReviewId())
//                    .productId(reviewDto.getProductId())
//                    .title(reviewDto.getTitle())
//                    .content(reviewDto.getContent())
//                    .starPoint(reviewDto.getStarPont())
//                    .createdAt(reviewDto.getCreatedAt())
//                    .build();
//        }

        public static List<ReviewDto> fromPage(Page<ReviewDto> page){
            return page.getContent();
        }

        public static List<ReviewDto> fromRawResult(List<Object[]> rawResult){
            return rawResult.stream()
                    .map(review -> {
                        BigInteger rawReviewId = (BigInteger) review[0];
                        Long reviewId = rawReviewId.longValue();
                        BigInteger rawProductId = (BigInteger) review[1];
                        Long productId = rawProductId.longValue();
                        String author = (String) review[2];
                        String title = (String) review[3];
                        String content = (String) review[4];
                        Integer starPoint = (Integer)review[5];
                        Timestamp timestamp = (Timestamp) review[6];
                        LocalDateTime createdAt = timestamp.toLocalDateTime();


                        return ReviewDto.builder()
                                .reviewId(reviewId)
                                .productId(productId)
                                .author(author)
                                .title(title)
                                .content(content)
                                .starPoint(starPoint)
                                .createdAt(createdAt)
                                .build();
                            }
                    ).collect(Collectors.toList());
        }

    }
}
