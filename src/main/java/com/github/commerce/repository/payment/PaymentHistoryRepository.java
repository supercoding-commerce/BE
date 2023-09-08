package com.github.commerce.repository.payment;

import com.github.commerce.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    Optional<PaymentHistory> findByIdAndProductId(Long paymentHistoryId, Long productId);
}
