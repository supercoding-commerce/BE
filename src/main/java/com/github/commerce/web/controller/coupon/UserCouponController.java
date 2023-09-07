package com.github.commerce.web.controller.coupon;

import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.coupon.UserCouponService;
import com.github.commerce.web.dto.coupon.UsersCouponResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "구매자의 쿠폰 사용 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/coupon/user")
public class UserCouponController {

    private final UserCouponService userCouponService;

    //본인의 쿠폰 목록 조회
    @ApiOperation("구매자의 쿠폰 목록 조회")
    @GetMapping
    public ResponseEntity<List<UsersCouponResponseDto>> getMyCouponList(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(userCouponService.getMyCouponList(userDetails.getUser().getEmail()));
    }

    //쿠폰 발급
//    @PostMapping("/issue")
//    public ResponseEntity<UsersCouponResponseDto> issueCoupon(@RequestBody UsersCouponIssueRequest usersCouponIssueRequest){
//        return ResponseEntity.ok(userCouponService.issueUserCoupon(usersCouponIssueRequest));
//    }
    @ApiOperation("구매자의 쿠폰 발급")
    @PostMapping("/issue")
    public ResponseEntity<UsersCouponResponseDto> issueCoupon(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam("couponId") Long couponId){
        return ResponseEntity.ok(userCouponService.issueUserCoupon(userDetails.getUser().getEmail(), couponId));
    }

    //쿠폰 사용 완료
    @ApiOperation("구매자의 쿠폰 사용 완료")
    @PatchMapping("/used")
    public ResponseEntity<UsersCouponResponseDto> usedCoupon(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam("couponId") Long couponId){
        return ResponseEntity.ok(userCouponService.usedUserCoupon(userDetails.getUser().getEmail(), couponId));
    }


    //TODO. 쿠폰 사용 기한 만료 - 서비스단? - @Schedule
}
