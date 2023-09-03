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
    USER_NICKNAME_ALREADY_EXIST("이미 존재하는 닉네임 입니다.",HttpStatus.CONFLICT);

    private final String description;
    private final HttpStatus httpStatus;
}
