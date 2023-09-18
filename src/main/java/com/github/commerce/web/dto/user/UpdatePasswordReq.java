package com.github.commerce.web.dto.user;

import lombok.Getter;

@Getter
public class UpdatePasswordReq {
    private String password;
    private String newPassword;
}
