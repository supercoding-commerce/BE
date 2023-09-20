package com.github.commerce.web.controller.coupon;

import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.coupon.CouponService;
import com.github.commerce.web.dto.coupon.CouponRegisterRequest;
import com.github.commerce.web.dto.coupon.CouponResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "쿠폰 조회 및 생성 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/coupon")
public class CouponController {

    private final CouponService couponService;

    //모든 쿠폰 조회
    @ApiOperation("쇼핑몰의 모든 쿠폰 조회")
    @GetMapping
    public ResponseEntity<List<CouponResponseDto>> getAllCoupons(){
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    //쿠폰 한 개 조회
    @ApiOperation("쿠폰 한 개 조회")
    @GetMapping("/{coupon_id}")
    public ResponseEntity<CouponResponseDto> getCouponByCouponId(@PathVariable("coupon_id")Long couponId){
        return ResponseEntity.ok(couponService.getCouponByCouponId(couponId));
    }

    //쿠폰 생성
    @ApiOperation("관리자가 쿠폰 생성")
    @PostMapping("/register")
    public ResponseEntity<CouponResponseDto> registerCoupon(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CouponRegisterRequest couponRegisterRequest){
        return ResponseEntity.ok(couponService.registerCoupon(userDetails, couponRegisterRequest));
    }

}
