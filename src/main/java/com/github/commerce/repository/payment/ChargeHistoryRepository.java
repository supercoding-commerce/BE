package com.github.commerce.repository.payment;

import com.github.commerce.entity.ChargeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargeHistoryRepository extends JpaRepository<ChargeHistory, Long> {
}
