package com.github.commerce.service.review.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReviewException extends RuntimeException {

    private ReviewErrorCode errorCode;
    private String errorMessage;

    public ReviewException(ReviewErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}