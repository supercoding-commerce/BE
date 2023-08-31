package com.github.commerce.web.controller.coupon;

import com.github.commerce.entity.Coupon;
import com.github.commerce.service.coupon.CouponService;
import com.github.commerce.web.dto.coupon.CouponRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/coupon")
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons(){
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("/{coupon_id}")
    public ResponseEntity<Coupon> getCouponByCouponId(@PathVariable("coupon_id")Long couponId){
        return ResponseEntity.ok(couponService.getCouponByCouponId(couponId));
    }

    @PostMapping("/register")
    public ResponseEntity<Coupon> registerCoupon(@RequestBody CouponRegisterRequest couponRegisterRequest){
        return ResponseEntity.ok(couponService.registerCoupon(couponRegisterRequest));
    }

}
