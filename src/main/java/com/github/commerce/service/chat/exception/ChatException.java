package com.github.commerce.service.chat.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatException extends RuntimeException {

    private ChatErrorCode errorCode;
    private String errorMessage;

    public ChatException(ChatErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
