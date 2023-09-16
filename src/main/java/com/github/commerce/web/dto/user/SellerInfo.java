package com.github.commerce.web.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SellerInfo {
    private String userName;
    private String telephone;
    private String shopName;
    private String address;
    private String addressDetail;
    private String shopImageUrl;
}
