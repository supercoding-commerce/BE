package com.github.commerce.web.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyInfoResponseDto {
    private String role;
    private String grade;
    private String nickname;
    private String address;
    private Long payMoney;
}
