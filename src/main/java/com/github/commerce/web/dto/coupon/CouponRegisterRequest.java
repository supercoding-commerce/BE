package com.github.commerce.web.dto.coupon;

import com.github.commerce.entity.Grade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CouponRegisterRequest {

    private String title;
    private String content;
    private Integer period;
    private String couponGrade;
    private Integer couponAmount;

}
