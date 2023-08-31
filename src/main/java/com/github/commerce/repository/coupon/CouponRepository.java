package com.github.commerce.repository.coupon;

import com.github.commerce.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findCouponById(Long couponId);
}
