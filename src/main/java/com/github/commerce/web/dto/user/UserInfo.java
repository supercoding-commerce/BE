package com.github.commerce.web.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfo {
    private String userName;
    private String telephone;
    private String address;
    private String addressDetail;
    private String nickname;
}
