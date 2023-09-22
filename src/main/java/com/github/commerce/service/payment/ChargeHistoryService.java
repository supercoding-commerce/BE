package com.github.commerce.service.payment;

import com.github.commerce.entity.ChargeHistory;
import com.github.commerce.entity.PayMoney;
import com.github.commerce.entity.User;
import com.github.commerce.repository.payment.ChargeHistoryRepository;
import com.github.commerce.repository.payment.PayMoneyRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.payment.exception.PaymentErrorCode;
import com.github.commerce.service.payment.exception.PaymentException;
import com.github.commerce.web.dto.payment.ChargeDto;
import com.github.commerce.web.dto.payment.ChargeHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ChargeHistoryService {

    private final UserRepository userRepository;
    private final PayMoneyRepository payMoneyRepository;
    private final ChargeHistoryRepository chargeHistoryRepository;

    @Transactional
    public ChargeDto.ChargeResponse chargePayMoney(Long userId, ChargeDto.ChargeRequest request) {
        // 사용자 조회: 주어진 userId로 사용자 정보를 조회합니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_USER_NOT_FOUND));

        // 사용자 페이머니 조회
        PayMoney payMoney = payMoneyRepository.findTop1ByUsersOrderByIdDesc(user)
                .orElseGet(() -> {
                    // 조회된 결과가 없는 경우, 새로운 PayMoney 객체 생성
                    PayMoney newPayMoney = new PayMoney();
                    newPayMoney.setUsers(user); // 사용자 ID 설정 등 다른 필요한 초기화 작업 수행
                    newPayMoney.setChargePayMoneyTotal(0L); // 초기값 설정
                    newPayMoney.setPayMoneyBalance(0L); // 초기값 설정
                    return payMoneyRepository.save(newPayMoney);
                });

        // 사용자의 페이머니 정보가 있는 경우: 페이머니 정보를 업데이트합니다.
        payMoney.setChargePayMoneyTotal(payMoney.getChargePayMoneyTotal() + request.getPayMoney());
        payMoney.setPayMoneyBalance(payMoney.getPayMoneyBalance() + request.getPayMoney());

        // 페이머니 정보를 데이터베이스에 저장하거나 업데이트합니다.
        payMoney = payMoneyRepository.save(payMoney);

        // 충전 내역 생성: 충전 내역을 생성하여 데이터베이스에 저장합니다.
        ChargeHistory chargeHistory = ChargeHistory.builder()
                .payMoney(request.getPayMoney())
                .paymentAmount(request.getPaymentAmount())
                .build();

        chargeHistory = chargeHistoryRepository.save(chargeHistory);

        // 페이머니 정보에 충전 내역을 연결합니다.
        payMoney.setChargeHistories(chargeHistory);

        // ChargeResponse DTO를 생성하여 반환합니다.
        return ChargeDto.ChargeResponse.from(ChargeHistoryDto.from(chargeHistory));
    }
}

