package com.github.commerce.service.cart.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CartException extends RuntimeException {

    private CartErrorCode errorCode;
    private String errorMessage;

    public CartException(CartErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

    public CartException(CartErrorCode errorCode, Object... args) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescriptionTemplate(args);
    }

}
