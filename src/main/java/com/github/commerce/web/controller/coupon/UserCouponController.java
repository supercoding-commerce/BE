package com.github.commerce.web.controller.coupon;

import com.github.commerce.service.coupon.UserCouponService;
import com.github.commerce.web.dto.coupon.UsersCouponIssueRequest;
import com.github.commerce.web.dto.coupon.UsersCouponResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/api/coupon/user")
public class UserCouponController {

    private final UserCouponService userCouponService;

    //본인의 쿠폰 목록 조회
    @GetMapping
    public ResponseEntity<List<UsersCouponResponseDto>> getMyCouponList(@RequestParam("id") String email){
        return ResponseEntity.ok(userCouponService.getMyCouponList(email));
    }

    //쿠폰 발급
//    @PostMapping("/issue")
//    public ResponseEntity<UsersCouponResponseDto> issueCoupon(@RequestBody UsersCouponIssueRequest usersCouponIssueRequest){
//        return ResponseEntity.ok(userCouponService.issueUserCoupon(usersCouponIssueRequest));
//    }
    @PostMapping("/issue")
    public ResponseEntity<UsersCouponResponseDto> issueCoupon(@RequestParam("userId")String userId, @RequestParam("couponId") Long couponId){
        return ResponseEntity.ok(userCouponService.issueUserCoupon(new UsersCouponIssueRequest(userId, couponId)));
    }

    //쿠폰 사용 완료
    @PatchMapping("/used")
    public ResponseEntity<UsersCouponResponseDto> usedCoupon(@RequestBody UsersCouponIssueRequest usersCouponIssueRequest){
        return ResponseEntity.ok(userCouponService.usedUserCoupon(usersCouponIssueRequest));
    }


    //TODO. 쿠폰 사용 기한 만료 - 서비스단? - @Schedule
}
