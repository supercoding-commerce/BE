package com.github.commerce.service.payment.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentException extends RuntimeException{

    private PaymentErrorCode errorCode;
    private String errorMessage;

    public PaymentException(PaymentErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }

}
