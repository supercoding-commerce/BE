package com.github.commerce.service.coupon;

import com.github.commerce.entity.Coupon;
import com.github.commerce.repository.coupon.CouponRepository;
import com.github.commerce.web.dto.coupon.CouponRegisterRequest;
import com.github.commerce.web.dto.coupon.CouponResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public List<CouponResponseDto> getAllCoupons(){
        return couponRepository.findAll().stream().map(CouponResponseDto::new).collect(Collectors.toList());
    }

    public CouponResponseDto getCouponByCouponId(Long couponId){
        return new CouponResponseDto(couponRepository.findCouponById(couponId));
    }

    @Transactional
    public CouponResponseDto registerCoupon(CouponRegisterRequest couponRegisterRequest){
        Coupon coupon = new Coupon(couponRegisterRequest);
        return new CouponResponseDto(couponRepository.save(coupon));
    }
}
