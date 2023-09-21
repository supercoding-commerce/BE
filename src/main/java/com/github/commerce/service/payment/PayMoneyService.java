package com.github.commerce.service.payment;

import com.github.commerce.entity.PayMoney;
import com.github.commerce.repository.payment.PayMoneyRepository;
import com.github.commerce.web.dto.payment.GetPayMoneyDto;
import com.github.commerce.web.dto.payment.PayMoneyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayMoneyService {

    private final PayMoneyRepository payMoneyRepository;

    @Transactional
    public List<GetPayMoneyDto.GetPayMoneyResponse> getPayMoneyList(Long userId) {
        List<PayMoney> payMoneyList = payMoneyRepository.findAllByUsersId(userId);

        if (payMoneyList == null || payMoneyList.isEmpty()) {
            // null 또는 빈 리스트인 경우 처리
            return Collections.emptyList(); // 또는 null을 반환하거나 다른 기본 값을 반환할 수 있음
        }

        List<GetPayMoneyDto.GetPayMoneyResponse> responseList = new ArrayList<>();

        for (PayMoney payMoney : payMoneyList) {
            Long payMoneyBalance = payMoney.calculatePayMoneyBalance();
            PayMoneyDto payMoneyDto = PayMoneyDto.fromEntity(payMoney);

            GetPayMoneyDto.GetPayMoneyResponse response = GetPayMoneyDto.GetPayMoneyResponse.builder()
                    .usedPayMoney(payMoneyDto.getUsedPayMoney())
                    .chargePayMoney(payMoney.getChargePayMoneyTotal())
                    .point(payMoneyDto.getPointBalance())
                    .payMoneyBalance(payMoneyBalance)
                    .createAt(payMoneyDto.getCreatedAt())
                    .build();

            responseList.add(response);
        }

        return responseList;
    }




}
