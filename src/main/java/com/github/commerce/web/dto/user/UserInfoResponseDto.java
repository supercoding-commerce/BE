package com.github.commerce.web.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserInfoResponseDto {
    private String email;
    private String password;
    private String userName;
    private String telephone;
    private String role;
    private String grade;
    private String gender;
    private String age;
    private String nickname;
    private String shopName;
    private String address;
}
