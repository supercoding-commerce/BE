package com.github.commerce.web.advice.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseDto<T> {
    private boolean result;
    private String msg;
    private T data;

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, "요청이 정상적으로 처리되었습니다.", data);
    }

    public static <T> ResponseDto<T> fail(String message) {
        return new ResponseDto<>(false, message,null);
    }
}