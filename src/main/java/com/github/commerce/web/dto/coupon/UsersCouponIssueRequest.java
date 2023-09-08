package com.github.commerce.web.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsersCouponIssueRequest {
    String userEmail;
    Long couponId;
}
