package com.github.commerce.service.product.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum  ProductErrorCode {
    //status(HttpStatus.BAD_REQUEST) 400
    TOO_MANY_FILES("본문에 삽입하 이미지 파일은 최대 5까지 가능합니다.",HttpStatus.BAD_REQUEST),


    //403
    NOT_REGISTERED_SELLER("판매자로 등록된 유저가 아닙니다.", HttpStatus.FORBIDDEN),

    //404
    NOTFOUND_PRODUCT("상품을 찾을 수 없습니다.",HttpStatus.NOT_FOUND),

    NOT_FOUND_FILE("파일이 존재하지 않습니다.",HttpStatus.NOT_FOUND),

    //500
    INTERNAL_SERVER_ERROR( "Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR),

    FAIL_TO_SAVE( "서버 측의 문제로 데이터의 저장에 실패했습니다. 다시 한 번 시도해주세요.",HttpStatus.INTERNAL_SERVER_ERROR);



    private final String description;
    private final HttpStatus httpStatus;
}
