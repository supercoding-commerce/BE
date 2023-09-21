package com.github.commerce.service.shop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ShopErrorCode {
    AUTHENTICATION_FAIL("인증에 실패하였습니다.", HttpStatus.UNAUTHORIZED);

    private final String description;
    private final HttpStatus httpStatus;
}
