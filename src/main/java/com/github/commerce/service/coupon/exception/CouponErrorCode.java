package com.github.commerce.service.coupon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CouponErrorCode {

    //status(HttpStatus.UNAUTHORIZED) 401
    ONLY_ADMIN_CAN_ACCESS("오직 관리자만 쿠폰을 생성할 수 있습니다.", HttpStatus.UNAUTHORIZED),
    ONLY_GREEN_CAN_ISSUE("Green 등급의 회원만 받을 수 있는 쿠폰입니다.", HttpStatus.UNAUTHORIZED),
    ONLY_ORANGE_CAN_ISSUE("Orange 등급의 회원만 받을 수 있는 쿠폰입니다.", HttpStatus.UNAUTHORIZED),
    ONLY_RED_CAN_ISSUE("Red 등급의 회원만 받을 수 있는 쿠폰입니다.", HttpStatus.UNAUTHORIZED),
    ONLY_VIP_CAN_ISSUE("VIP 등급의 회원만 받을 수 있는 쿠폰입니다.", HttpStatus.UNAUTHORIZED),

    //status(HttpStatus.NOT_FOUND) 404
    USER_DOES_NOT_HAVE_THIS_COUPON("유저가 해당 쿠폰을 가지고 있지 않습니다.", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND),
    THIS_COUPON_DOES_NOT_EXIST("존재하지 않는 쿠폰입니다.", HttpStatus.NOT_FOUND),
    USER_INFO_NOT_FOUND("사용자 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    //status(HttpStatus.CONFLICT) 409
    OUT_OF_STOCK("쿠폰 발급 수량이 소진되었습니다.", HttpStatus.CONFLICT),
    COUPON_ALREADY_EXISTS("쿠폰이 이미 발급되었습니다.", HttpStatus.CONFLICT);

    private final String description;
    private final HttpStatus httpStatus;
}
