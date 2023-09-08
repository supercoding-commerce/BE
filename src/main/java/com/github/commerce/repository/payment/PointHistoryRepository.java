package com.github.commerce.repository.payment;

import com.github.commerce.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory,Long> {
}
