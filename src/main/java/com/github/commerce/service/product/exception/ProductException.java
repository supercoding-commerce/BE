package com.github.commerce.service.product.exception;

import com.github.commerce.service.user.exception.UserErrorCode;
import com.github.commerce.web.advice.exception.type.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductException extends RuntimeException {
    private final ProductErrorCode errorCode;
    private String errorMessage;

    public ProductException(ProductErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage=errorCode.getDescription();
    }
}
