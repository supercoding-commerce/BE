package com.github.commerce.service.coupon;

import com.github.commerce.entity.Coupon;
import com.github.commerce.entity.User;
import com.github.commerce.entity.UsersCoupon;
import com.github.commerce.repository.coupon.CouponRepository;
import com.github.commerce.repository.coupon.UsersCouponRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.coupon.exception.CouponErrorCode;
import com.github.commerce.service.coupon.exception.CouponException;
import com.github.commerce.web.dto.coupon.UsersCouponIssueRequest;
import com.github.commerce.web.dto.coupon.UsersCouponResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserCouponService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UsersCouponRepository usersCouponRepository;

    //본인의 쿠폰 목록 조회
    public List<UsersCouponResponseDto> getMyCouponList(String userEmail){

        User user = userRepository.findUserByEmail(userEmail);

        //등록되지 않은 아이디일 때
        if(user==null || user.getIsDelete()){
            throw new CouponException(CouponErrorCode.USER_NOT_FOUND);
        }

        return usersCouponRepository.findUsersCouponByUsersIdAndExpiredAtAfterAndIsUsedOrderByExpiredAt(user.getId(), LocalDateTime.now(), false).stream().map(UsersCouponResponseDto::new).collect(Collectors.toList());
    }

    //회원이 쿠폰 발급
    //synchronized 키워드
    @Transactional
    public synchronized UsersCouponResponseDto issueUserCoupon(UsersCouponIssueRequest usersCouponIssueRequest){

        User user = userRepository.findUserByEmail(usersCouponIssueRequest.getUserEmail());
        Coupon coupon = couponRepository.findCouponById(usersCouponIssueRequest.getCouponId());

        //등록되지 않은 아이디일 때
        if(user==null || user.getIsDelete()){
            throw new CouponException(CouponErrorCode.USER_NOT_FOUND);
        }

        //등록되지 않은 쿠폰일 때
        if(coupon == null){
            throw new CouponException(CouponErrorCode.THIS_COUPON_DOES_NOT_EXIST);
        }

        //해당 유저가 동일한 쿠폰을 발급받은 적이 있을 때
        if(usersCouponRepository.existsByUsersIdAndCouponsId(user.getId(), usersCouponIssueRequest.getCouponId())){
            throw new CouponException(CouponErrorCode.COUPON_ALREADY_EXISTS);
        }

        //쿠폰이 모두 소진되었을 때
        if(coupon.getCouponAmount()<=0){
            throw new CouponException(CouponErrorCode.OUT_OF_STOCK);
        }

        //TODO. 현재 회원의 등급이 쿠폰 발급 가능 회원 등급에 해당하지 않을 때 예외처리

        //쿠폰 한 개 사용
        coupon.setCouponAmount(coupon.getCouponAmount()-1);
        couponRepository.save(coupon);
        return new UsersCouponResponseDto(usersCouponRepository.save(new UsersCoupon(coupon, user, LocalDateTime.now().plusDays(coupon.getPeriod()))));
    }

    //쿠폰을 사용완료 했을 때
    public UsersCouponResponseDto usedUserCoupon(UsersCouponIssueRequest usersCouponIssueRequest) {

        User user = userRepository.findUserByEmail(usersCouponIssueRequest.getUserEmail());
        Coupon coupon = couponRepository.findCouponById(usersCouponIssueRequest.getCouponId());

        //등록되지 않은 아이디일 때
        if(user==null || user.getIsDelete()){
            throw new CouponException(CouponErrorCode.USER_NOT_FOUND);
        }

        //등록되지 않은 쿠폰일 때
        if(coupon == null){
            throw new CouponException(CouponErrorCode.THIS_COUPON_DOES_NOT_EXIST);
        }

        UsersCoupon usersCoupon = usersCouponRepository.findUsersCouponByUsersIdAndCouponsIdAndExpiredAtAfterAndIsUsed(user.getId(), coupon.getId(), LocalDateTime.now(), false);

        //회원이 갖고 있는 쿠폰이 아닐 때
        if(usersCoupon == null){
            throw new CouponException(CouponErrorCode.USER_DOES_NOT_HAVE_THIS_COUPON);
        }

        usersCoupon.setIsUsed(true);

        return new UsersCouponResponseDto(usersCouponRepository.save(usersCoupon));
    }
}
