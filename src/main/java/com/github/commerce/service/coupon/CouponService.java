package com.github.commerce.service.coupon;

import com.github.commerce.entity.Coupon;
import com.github.commerce.entity.Grade;
import com.github.commerce.entity.UsersInfo;
import com.github.commerce.repository.coupon.CouponRepository;
import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.repository.user.UserInfoRepository;
import com.github.commerce.service.coupon.exception.CouponErrorCode;
import com.github.commerce.service.coupon.exception.CouponException;
import com.github.commerce.web.dto.coupon.CouponRegisterRequest;
import com.github.commerce.web.dto.coupon.CouponResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserInfoRepository userInfoRepository;

    //전체 쿠폰 목록 조회
    public List<CouponResponseDto> getAllCoupons(){
        return couponRepository.findAll().stream().map(CouponResponseDto::new).collect(Collectors.toList());
    }

    //쿠폰아이디로 쿠폰 조회
    public CouponResponseDto getCouponByCouponId(Long couponId){
        return new CouponResponseDto(couponRepository.findCouponById(couponId));
    }

    //쿠폰 생성
    @Transactional
    public CouponResponseDto registerCoupon(UserDetailsImpl userDetails, CouponRegisterRequest couponRegisterRequest){
        Long userId = userDetails.getUser().getId();
        Optional<UsersInfo> usersInfo = userInfoRepository.findByUsersId(userId);

        //사용자 정보가 없을 때
        if (usersInfo.isEmpty()) {
            throw new CouponException(CouponErrorCode.USER_INFO_NOT_FOUND);
        }

        //쿠폰을 생성하려는 사람이 관리자가 아닐 때
        if(usersInfo.get().getGrade()!= Grade.ADMIN){
            throw new CouponException(CouponErrorCode.ONLY_ADMIN_CAN_ACCESS);
        }

        //같은 이름의 쿠폰이 있을 때
        if(couponRepository.existsByTitleAndIsDeleted(couponRegisterRequest.getTitle(), false)){
            throw new CouponException(CouponErrorCode.SAME_NAME_COUPON_EXISTS);
        }

        Coupon coupon = new Coupon(couponRegisterRequest);
        return new CouponResponseDto(couponRepository.save(coupon));
    }
}
