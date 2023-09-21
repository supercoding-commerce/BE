package com.github.commerce.repository.payment;

import com.github.commerce.entity.PayMoney;
import com.github.commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayMoneyRepository extends JpaRepository<PayMoney, Long> {

    // 보유한 페이머니 , findTop1By -> 1개만 가져온다.
    Optional<PayMoney> findTop1ByUsersOrderByIdDesc(User user);

    Optional<PayMoney> findByUsersId(Long userId);

    List<PayMoney> findAllByUsersId(Long userId);

    List<PayMoney> findAllByUsersIdOrderByCreatedAtDesc(Long userId);
}
