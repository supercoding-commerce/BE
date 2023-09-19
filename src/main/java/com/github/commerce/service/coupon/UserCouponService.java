package com.github.commerce.service.coupon;

import com.github.commerce.entity.Coupon;
import com.github.commerce.entity.Grade;
import com.github.commerce.entity.User;
import com.github.commerce.entity.UsersCoupon;
import com.github.commerce.repository.coupon.CouponRepository;
import com.github.commerce.repository.coupon.UsersCouponRepository;
import com.github.commerce.repository.user.UserInfoRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.coupon.exception.CouponErrorCode;
import com.github.commerce.service.coupon.exception.CouponException;
import com.github.commerce.web.dto.coupon.UsersCouponResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserCouponService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UsersCouponRepository usersCouponRepository;
    private final UserInfoRepository userInfoRepository;

    //본인의 쿠폰 목록 조회
    public List<UsersCouponResponseDto> getMyCouponList(Long userId){

        Optional<User> user = userRepository.findById(userId);

        //등록되지 않은 아이디일 때
        if(user.isEmpty() || user.get().getIsDelete()){
            throw new CouponException(CouponErrorCode.USER_NOT_FOUND);
        }

        return usersCouponRepository.findUsersCouponByUsersIdAndExpiredAtAfterOrderByExpiredAt(user.get().getId(), LocalDateTime.now()).stream().map(UsersCouponResponseDto::new).collect(Collectors.toList());
    }

    //회원이 쿠폰 1개 발급
    //동시성 처리
    @Transactional
    public UsersCouponResponseDto issueUserCoupon(Long userId, Long couponId) {

        Optional<User> user = userRepository.findById(userId);
        Coupon coupon = couponRepository.findCouponById(couponId);

        //등록되지 않은 아이디일 때
        if (user.isEmpty() || user.get().getIsDelete()) {
            throw new CouponException(CouponErrorCode.USER_NOT_FOUND);
        }

        //등록되지 않은 쿠폰일 때
        if (coupon == null) {
            throw new CouponException(CouponErrorCode.THIS_COUPON_DOES_NOT_EXIST);
        }

        //해당 유저가 동일한 쿠폰을 발급받은 적이 있을 때
        if(usersCouponRepository.existsByUsersIdAndCouponsId(user.get().getId(), couponId)){
            throw new CouponException(CouponErrorCode.COUPON_ALREADY_EXISTS);
        }

        //회원 정보가 없을 때
        if(userInfoRepository.findByUsersId(user.get().getId()).isEmpty()){
            throw new CouponException(CouponErrorCode.USER_INFO_NOT_FOUND);
        }

        //현재 회원의 등급이 쿠폰 발급 가능 회원 등급에 해당하지 않을 때
        Grade grade = userInfoRepository.findByUsersId(user.get().getId()).get().getGrade();
        if( grade != Grade.ALL && grade != coupon.getCouponGrade()){
            if(coupon.getCouponGrade()==Grade.GREEN){
                throw new CouponException(CouponErrorCode.ONLY_GREEN_CAN_ISSUE);
            }
            else if(coupon.getCouponGrade()==Grade.ORANGE){
                throw new CouponException(CouponErrorCode.ONLY_ORANGE_CAN_ISSUE);
            }
            else if(coupon.getCouponGrade()==Grade.RED){
                throw new CouponException(CouponErrorCode.ONLY_RED_CAN_ISSUE);
            }
            else if(coupon.getCouponGrade()==Grade.VIP){
                throw new CouponException(CouponErrorCode.ONLY_VIP_CAN_ISSUE);
            }
        }

        //쿠폰 한 개 사용
        /** synchronized
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
         **/
        coupon.decreaseCouponAmount(1);

        return new UsersCouponResponseDto(usersCouponRepository.save(new UsersCoupon(coupon, user.get(), LocalDateTime.now().plusDays(coupon.getPeriod()))));
    }

    //쿠폰을 사용완료 했을 때
    @Transactional //update이므로 @Transactional 사용
    public UsersCouponResponseDto usedUserCoupon(Long userId, Long couponId) {

        Optional<User> user = userRepository.findById(userId);
        Coupon coupon = couponRepository.findCouponById(couponId);

        //등록되지 않은 아이디일 때
        if (user.isEmpty() || user.get().getIsDelete()) {
            throw new CouponException(CouponErrorCode.USER_NOT_FOUND);
        }

        //등록되지 않은 쿠폰일 때
        if(coupon == null){
            throw new CouponException(CouponErrorCode.THIS_COUPON_DOES_NOT_EXIST);
        }

        UsersCoupon usersCoupon = usersCouponRepository.findUsersCouponByUsersIdAndCouponsIdAndExpiredAtAfterAndIsUsed(user.get().getId(), coupon.getId(), LocalDateTime.now(), false);

        //회원이 갖고 있는 쿠폰이 아닐 때
        if(usersCoupon == null){
            throw new CouponException(CouponErrorCode.USER_DOES_NOT_HAVE_THIS_COUPON);
        }

        // TODO : 원활한 테스트를 위하여 false로 설정, 마무리 작업시 다시 true로 변경예정
        usersCoupon.setIsUsed(false);

        return new UsersCouponResponseDto(usersCouponRepository.save(usersCoupon));
    }

    //만료 날짜 지난 쿠폰 주기적으로 삭제 - 삭제 일시: 매일 오전 12:00:00
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") //초 분 시 일 월 요일 (*: 매번)
    @Transactional
    public void deleteExpiredUsersCoupon(){
        log.info("유효기간 만료 쿠폰 삭제 = {}", LocalDateTime.now());
        usersCouponRepository.deleteUsersCouponByExpiredAtBefore(LocalDateTime.now());
    }
}
