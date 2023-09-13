package com.github.commerce.service.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode {

    //status(HttpStatus.CONFLICT) 409
    USER_EMAIL_ALREADY_EXIST("이미 존재하는 email 입니다.",HttpStatus.CONFLICT),
    SHOP_NAME_ALREADY_EXIST("이미 존재하는 쇼핑몰 이름 입니다.",HttpStatus.CONFLICT),
    USER_NICKNAME_ALREADY_EXIST("이미 존재하는 닉네임 입니다.",HttpStatus.CONFLICT),

    //status(HttpStatus.BAD_REQUEST) 400
    LOGIN_FAIL("로그인에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    UER_NOT_FOUND("존재하지 않는 유저입니다.", HttpStatus.BAD_REQUEST),
    NOT_USER("판매자는 찜을 할 수 없습니다.",HttpStatus.BAD_REQUEST),

    //status(HttpStatus.UNAUTHORIZED) 401
    AUTHENTICATION_FAIL("인증에 실패하였습니다.",HttpStatus.UNAUTHORIZED);

    private final String description;
    private final HttpStatus httpStatus;
}
