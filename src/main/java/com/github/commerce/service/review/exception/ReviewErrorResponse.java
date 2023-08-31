package com.github.commerce.service.review.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewErrorResponse {

    private ReviewErrorCode errorCode;
    private String errorMessage;
}