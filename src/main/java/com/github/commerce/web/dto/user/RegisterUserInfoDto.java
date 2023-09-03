package com.github.commerce.web.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserInfoDto extends RegisterUserDto{
    private String gender;
    private String address;
    private String age;
    private String nickname;
}
