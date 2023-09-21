package com.github.commerce.service.payment;

import com.github.commerce.entity.PayMoney;
import com.github.commerce.entity.PointHistory;
import com.github.commerce.repository.payment.PayMoneyRepository;
import com.github.commerce.repository.payment.PointHistoryRepository;
import com.github.commerce.service.payment.exception.PaymentErrorCode;
import com.github.commerce.service.payment.exception.PaymentException;
import com.github.commerce.web.dto.payment.PointHistoryDto;
import com.github.commerce.web.dto.payment.PointStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

    private final PayMoneyRepository payMoneyRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public void updatePayMoneyAndAddPointHistory(Long userId, Long earnedPoint, Long usedPoint) {

        List<PayMoney> payMoneyList = payMoneyRepository.findAllByUsersIdOrderByCreatedAtDesc(userId);

        if (payMoneyList.isEmpty()) {
            // 아직 아무런 포인트 내역이 없는 경우
            throw new PaymentException(PaymentErrorCode.POINT_HISTORY_NO_RECORDS_FOUND);
        }

        PayMoney payMoney = payMoneyList.get(0);

        // 포인트 잔액 업데이트
        Long currentPointBalance = payMoney.getPointBalance();
        Long newPointBalance = currentPointBalance + earnedPoint - usedPoint;
        payMoney.setPointBalance(newPointBalance);
        payMoneyRepository.save(payMoney);

        // 포인트 내역 추가
        PointHistory pointHistory = new PointHistory();
        pointHistory.setPayMoney(payMoney);
        pointHistory.setEarnedPoint(earnedPoint);
        pointHistory.setUsedPoint(usedPoint);


        // 조건에 따라 상태값 설정
        if (earnedPoint > 0) {
            pointHistory.setStatus(PointStatusEnum.EARN_POINT.ordinal());
        } else if (usedPoint < 0) {
            pointHistory.setStatus(PointStatusEnum.USE_POINT.ordinal());
        } else{
            throw new PaymentException(PaymentErrorCode.POINT_HISTORY_INVALID_POINT);
        }

        pointHistoryRepository.save(pointHistory);
    }

    public List<PointHistoryDto> getPointHistoryList(Long userId) {
        List<PointHistory> pointHistoryList = pointHistoryRepository.findAllByPayMoneyUsersId(userId);
        List<PointHistoryDto> pointHistoryDtoList = pointHistoryList.stream()
                .map(PointHistoryDto::fromEntity)
                .collect(Collectors.toList());
        return pointHistoryDtoList;
    }
}
