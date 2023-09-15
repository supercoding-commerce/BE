package com.github.commerce.service.review.exception;

import com.github.commerce.service.product.exception.ProductErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode {

    //400
    IMAGE_EMPTY("이미지 파일이 없습니다.", HttpStatus.BAD_REQUEST ),

    //status(HttpStatus.FORBIDDEN) 403
    REVIEW_PERMISSION_DENIED("당신은 이 상품을 산 적이 없는데요.", HttpStatus.FORBIDDEN),
    NO_PERMISSION_TO_UPDATE("이 상품에 대한 리뷰를 수정할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    NO_PERMISSION_TO_DELETE("삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),

    //status(HttpStatus.NOT_FOUND) 404
    USER_INFO_NOT_FOUD("회원 정보를 추가해주세요.", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("존재하지 않는 유저 입니다.", HttpStatus.NOT_FOUND),
    THIS_PRODUCT_DOES_NOT_EXIST("존재하지 않는 상품 입니다.", HttpStatus.NOT_FOUND),
    REVIEW_DOES_NOT_EXIST("리뷰가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    PAYMONEY_NOT_FOUD("지갑 정보가 없습니다", HttpStatus.NOT_FOUND),

    //status(HttpStatus.CONFLICT) 409
    REVIEW_ALREADY_EXISTS("이미 리뷰를 작성하셨는데요.", HttpStatus.CONFLICT),
    PAYMENT_POINT_NULL("포인트 적립금 정보가 없습니다.", HttpStatus.CONFLICT),

    //status(HttpStatus.UNPROCESSABLE_ENTITY) 422
    FAILED_UPLOAD("이미지 업로드에 실패했습니다.", HttpStatus.UNPROCESSABLE_ENTITY);


    private final String description;
    private final HttpStatus httpStatus;
}