package com.github.commerce.service.coupon.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponErrorResponse {

    private CouponErrorCode errorCode;
    private String errorMessage;
}
