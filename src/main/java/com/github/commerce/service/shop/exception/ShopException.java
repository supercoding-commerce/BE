package com.github.commerce.service.shop.exception;

import lombok.Getter;

@Getter
public class ShopException extends RuntimeException{

    private ShopErrorCode errorCode;
    private String errorMessage;

    public ShopException(ShopErrorCode errorCode) {
        this.errorCode=errorCode;
        this.errorMessage=errorCode.getDescription();
    }
}
