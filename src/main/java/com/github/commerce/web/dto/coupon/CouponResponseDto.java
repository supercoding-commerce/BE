package com.github.commerce.web.dto.coupon;

import com.github.commerce.entity.Coupon;
import com.github.commerce.entity.Grade;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CouponResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Integer period;
    private Grade couponGrade;

    public CouponResponseDto(Coupon coupon){
        this.id = coupon.getId();
        this.title = coupon.getTitle();
        this.content = coupon.getContent();
        this.createdAt = coupon.getCreatedAt();
        this.period = coupon.getPeriod();
        this.couponGrade = coupon.getCouponGrade();
    }
}
