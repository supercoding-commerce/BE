package com.github.commerce.service.order.exception;

import com.github.commerce.service.cart.exception.CartErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderException extends RuntimeException {

    private OrderErrorCode errorCode;
    private String errorMessage;

    public OrderException(OrderErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
