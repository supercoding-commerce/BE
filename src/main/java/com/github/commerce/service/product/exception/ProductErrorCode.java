package com.github.commerce.service.product.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum  ProductErrorCode {

    INTERNAL_SERVER_ERROR( "Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR),

    FAIL_TO_SAVE( "서버 측의 문제로 데이터의 저장에 실패했습니다. 다시 한 번 시도해주세요.",HttpStatus.CONFLICT),

    NOT_FOUND_FILE("파일이 존재하지 않습니다.",HttpStatus.NOT_FOUND);

    private final String description;
    private final HttpStatus httpStatus;
}
