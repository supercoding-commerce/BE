package com.github.commerce.web.dto.coupon;

import com.github.commerce.entity.UsersCoupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UsersCouponResponseDto {

    private String userEmail;
    private Long couponId;
    private String couponTitle;
    private String couponContent;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private Boolean isUsed;

    public UsersCouponResponseDto(UsersCoupon usersCoupon){
        this.userEmail = usersCoupon.getUsers().getEmail();
        this.couponId = usersCoupon.getCoupons().getId();
        this.couponTitle = usersCoupon.getCoupons().getTitle();
        this.couponContent = usersCoupon.getCoupons().getContent();
        this.createdAt = usersCoupon.getCreatedAt();
        this.expiredAt = usersCoupon.getExpiredAt();
        this.isUsed = usersCoupon.getIsUsed();
    }

}
