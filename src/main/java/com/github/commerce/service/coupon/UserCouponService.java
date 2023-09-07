package com.github.commerce.service.coupon;

import com.github.commerce.entity.Coupon;
import com.github.commerce.entity.User;
import com.github.commerce.entity.UsersCoupon;
import com.github.commerce.repository.coupon.CouponRepository;
import com.github.commerce.repository.coupon.UsersCouponRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.coupon.exception.CouponErrorCode;
import com.github.commerce.service.coupon.exception.CouponException;
import com.github.commerce.web.dto.coupon.UsersCouponResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserCouponService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UsersCouponRepository usersCouponRepository;

    //본인의 쿠폰 목록 조회
    public List<UsersCouponResponseDto> getMyCouponList(String userEmail){
        //String userEmail = userDetails.getUser().getEmail();
        User user = userRepository.findUserByEmail(userEmail);

        //등록되지 않은 아이디일 때
        if(user==null || user.getIsDelete()){
            throw new CouponException(CouponErrorCode.USER_NOT_FOUND);
        }

        return usersCouponRepository.findUsersCouponByUsersIdAndExpiredAtAfterOrderByExpiredAt(user.getId(), LocalDateTime.now()).stream().map(UsersCouponResponseDto::new).collect(Collectors.toList());
    }

    //회원이 쿠폰 1개 발급
    //동시성 처리
    @Transactional
    public UsersCouponResponseDto issueUserCoupon(String userEmail, Long couponId) {
//        log.info("email={}", userDetails.getUser().getEmail());
//        User user = userRepository.findUserByEmail(userDetails.getUser().getEmail());
//        //log.info("id={}", userDetails.getUser().getId());
//        //Optional<User> user = userRepository.findById(userDetails.getUser().getId());
        User user = userRepository.findUserByEmail(userEmail);
        Coupon coupon = couponRepository.findCouponById(couponId);

        //등록되지 않은 아이디일 때
        if (user==null || user.getIsDelete()) {
            throw new CouponException(CouponErrorCode.USER_NOT_FOUND);
        }

        //등록되지 않은 쿠폰일 때
        if (coupon == null) {
            throw new CouponException(CouponErrorCode.THIS_COUPON_DOES_NOT_EXIST);
        }

        //TODO. 임시 주석 - 등급 구분할 수 있을 때 풀기
//        //해당 유저가 동일한 쿠폰을 발급받은 적이 있을 때
//        if(usersCouponRepository.existsByUsersIdAndCouponsId(user.getId(), usersCouponIssueRequest.getCouponId())){
//            throw new CouponException(CouponErrorCode.COUPON_ALREADY_EXISTS);
//        }

        //TODO. 현재 회원의 등급이 쿠폰 발급 가능 회원 등급에 해당하지 않을 때 예외처리

        //쿠폰 한 개 사용 - synchronized
//        synchronized (this){ //synchronized 키워드 -> UserCouponService 인스턴스에 락이 걸리게 됨. -> 성능 이슈
//            //쿠폰이 모두 소진되었을 때
//            if(coupon.getCouponAmount()<=0){
//                throw new CouponException(CouponErrorCode.OUT_OF_STOCK);
//            }
//            log.info("stock={}", coupon.getCouponAmount());
//            coupon.setCouponAmount(coupon.getCouponAmount()-1);
//            couponRepository.save(coupon);
//        }

//        if (coupon.getCouponAmount() <= 0) {
//            throw new CouponException(CouponErrorCode.OUT_OF_STOCK);
//        }
//        log.info("stock={}", coupon.getCouponAmount());
//        coupon.setCouponAmount(coupon.getCouponAmount() - 1);
//        couponRepository.save(coupon);
        coupon.decreaseCouponAmount(1);

        return new UsersCouponResponseDto(usersCouponRepository.save(new UsersCoupon(coupon, user, LocalDateTime.now().plusDays(coupon.getPeriod()))));
    }

    //쿠폰을 사용완료 했을 때
    public UsersCouponResponseDto usedUserCoupon(String userEmail, Long couponId) {
        User user = userRepository.findUserByEmail(userEmail);
        //User user = userRepository.findUserByEmail(userDetails.getUser().getEmail());
        Coupon coupon = couponRepository.findCouponById(couponId);

        //등록되지 않은 아이디일 때
        if (user==null || user.getIsDelete()) {
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
