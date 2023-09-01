package com.github.commerce.service.coupon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CouponErrorCode {

    //status(HttpStatus.BAD_REQUEST) 400
    USER_DOES_NOT_HAVE_THIS_COUPON("유저가 해당 쿠폰을 가지고 있지 않습니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("존재하지 않는 유저입니다.", HttpStatus.BAD_REQUEST),
    THIS_COUPON_DOES_NOT_EXIST("존재하지 않는 쿠폰입니다.", HttpStatus.BAD_REQUEST),

    //status(HttpStatus.CONFLICT) 409
    OUT_OF_STOCK("쿠폰 발급 수량이 소진되었습니다.", HttpStatus.CONFLICT),
    COUPON_ALREADY_EXISTS("쿠폰이 이미 발급되었습니다.", HttpStatus.CONFLICT);


    private final String description;
    private final HttpStatus httpStatus;
}
