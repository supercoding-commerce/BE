package com.github.commerce.service.chat.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatErrorResponse {

    private ChatErrorCode errorCode;
    private String errorMessage;
}

