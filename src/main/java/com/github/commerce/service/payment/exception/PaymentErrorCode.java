package com.github.commerce.service.payment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PaymentErrorCode {

    PAYMENT_INSUFFICIENT_BALANCE("잔액이 부족합니다.", HttpStatus.BAD_REQUEST),
    PAYMENT_INVALID_COUPON("유효하지 않은 쿠폰 코드입니다.", HttpStatus.BAD_REQUEST),
    PAYMENT_INVALID_ORDER("유효하지 않은 주문입니다.", HttpStatus.BAD_REQUEST),
    PAYMENT_USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PAYMENT_INVALID_DISCOUNT("할인 금액이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    PAYMENT_ORDER_ALREADY_COMPLETED("유효하지 않은 주문번호 입니다.", HttpStatus.BAD_REQUEST),

//    PAYMENT_DUPLICATE_COUPON_USAGE("중복된 쿠폰 사용은 허용되지 않습니다.", HttpStatus.BAD_REQUEST);

    PAY_MONEY_NO_RECORDS_FOUND("해당 사용자의 페이머니 내역이 없습니다", HttpStatus.BAD_REQUEST),
    POINT_HISTORY_NO_RECORDS_FOUND("해당 사용자의 포인트 내역이 없습니다.", HttpStatus.BAD_REQUEST),
    POINT_HISTORY_INVALID_POINT("적립 및 사용 포인트가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    CHARGE_PAY_MONEY_SELLER_OPERATION_FORBIDDEN("SELLER는 이 작업을 수행할 수 없습니다.",HttpStatus.FORBIDDEN ),


    ;



    private final String description;
    private final HttpStatus httpStatus;
}
