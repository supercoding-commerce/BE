package com.github.commerce.service.user.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserErrorResponse {
    private UserErrorCode errorCode;
    private String errorMessage;

}
