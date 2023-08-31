package com.github.commerce.service.coupon;

import com.github.commerce.entity.Coupon;
import com.github.commerce.repository.coupon.CouponRepository;
import com.github.commerce.web.dto.coupon.CouponRegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public List<Coupon> getAllCoupons(){
        return couponRepository.findAll();
    }

    public Coupon getCouponByCouponId(Long couponId){
        return couponRepository.findCouponById(couponId);
    }

    @Transactional
    public Coupon registerCoupon(CouponRegisterRequest couponRegisterRequest){
        Coupon coupon = new Coupon(couponRegisterRequest);
        return couponRepository.save(coupon);
    }
}
