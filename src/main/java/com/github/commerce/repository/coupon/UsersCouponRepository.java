package com.github.commerce.repository.coupon;

import com.github.commerce.entity.UsersCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsersCouponRepository extends JpaRepository<UsersCoupon, Long> {

    boolean existsByUsersIdAndCouponsId(Long id, Long couponId);

    List<UsersCoupon> findUsersCouponByUsersIdAndExpiredAtAfterOrderByExpiredAt(Long id, LocalDateTime now);

    UsersCoupon findUsersCouponByUsersIdAndCouponsIdAndExpiredAtAfterAndIsUsed(Long id, Long id1, LocalDateTime now, boolean isUsed);
}
