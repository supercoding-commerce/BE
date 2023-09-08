package com.github.commerce.web.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDto {
    private String email;
    private String password;
    private String userName;
    private String telephone;
}
