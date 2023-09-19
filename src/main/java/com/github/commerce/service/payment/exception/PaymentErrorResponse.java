package com.github.commerce.service.payment.exception;

import com.github.commerce.service.coupon.exception.CouponErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentErrorResponse {
    private PaymentErrorCode errorCode;
    private String errorMessage;
}
