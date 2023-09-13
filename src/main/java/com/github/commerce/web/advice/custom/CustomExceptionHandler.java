package com.github.commerce.web.advice.custom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<?> handleApiRequestException(CustomException ex) {
        HttpStatus httpStatus = ex.getErrorCode().getHttpStatus();
        String errMSG = ex.getErrorCode().getErrorMsg();

        return new ResponseEntity<>(ResponseDto.fail(errMSG), httpStatus);
    }
}
