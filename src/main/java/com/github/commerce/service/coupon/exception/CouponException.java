package com.github.commerce.service.coupon.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouponException extends RuntimeException{

    private CouponErrorCode errorCode;
    private String errorMessage;

    public CouponException(CouponErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

}
