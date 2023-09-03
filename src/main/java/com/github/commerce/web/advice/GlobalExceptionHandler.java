package com.github.commerce.web.advice;

import com.github.commerce.service.cart.exception.CartErrorResponse;
import com.github.commerce.service.cart.exception.CartException;
import com.github.commerce.service.coupon.exception.CouponErrorResponse;
import com.github.commerce.service.coupon.exception.CouponException;
import com.github.commerce.service.order.exception.OrderErrorResponse;
import com.github.commerce.service.order.exception.OrderException;
import com.github.commerce.service.review.exception.ReviewErrorResponse;
import com.github.commerce.service.review.exception.ReviewException;
import com.github.commerce.service.user.exception.UserErrorResponse;
import com.github.commerce.service.user.exception.UserException;
import com.github.commerce.web.advice.exception.ErrorResponse;
import com.github.commerce.web.advice.exception.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(
            MethodArgumentNotValidException e
    ){
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e
    ){
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.BAD_REQUEST,
                "Request Body가 비어 있습니다");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(CartException.class)
    public ResponseEntity<?> handleCartException(CartException e){

        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CartErrorResponse.builder()
                        .errorCode(e.getErrorCode())
                        .errorMessage(e.getErrorMessage())
                        .build());

    }

    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<?> handleReviewException(ReviewException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ReviewErrorResponse.builder()
                        .errorCode(e.getErrorCode())
                        .errorMessage(e.getErrorMessage())
                        .build());
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<?> handleOrderException(OrderException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(OrderErrorResponse.builder()
                        .errorCode(e.getErrorCode())
                        .errorMessage(e.getErrorMessage())
                        .build());
    }

    @ExceptionHandler(CouponException.class)
    public ResponseEntity<?> handleCouponException(CouponException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(CouponErrorResponse.builder()
                        .errorCode(e.getErrorCode())
                        .errorMessage(e.getErrorMessage())
                        .build());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<?> handleUserException(UserException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(UserErrorResponse.builder()
                        .errorCode(e.getErrorCode())
                        .errorMessage(e.getErrorMessage())
                        .build());
    }

}
