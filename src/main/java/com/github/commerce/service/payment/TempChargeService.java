//package com.github.commerce.service.payment;
//
//import com.github.commerce.entity.PayMoney;
//import com.github.commerce.entity.User;
//import com.github.commerce.repository.payment.PayMoneyRepository;
//import com.github.commerce.repository.user.UserRepository;
//import com.github.commerce.service.payment.exception.PaymentErrorCode;
//import com.github.commerce.service.payment.exception.PaymentException;
//import com.github.commerce.web.dto.payment.TempPayMoneyDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class TempChargeService {
//
//    private final UserRepository userRepository;
//    private final PayMoneyRepository payMoneyRepository;
//
//    @Transactional
//    public TempPayMoneyDto tempCharge(Long userId) {
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_USER_NOT_FOUND));
//
//        PayMoney payMoney = payMoneyRepository.findByUsersId(userId).orElse(null);
//
//        if (payMoney == null) {
//            payMoney = PayMoney.builder()
//                    .users(user)
//                    .chargePayMoneyTotal(1000000L)
//                    .payMoneyBalance(1000000L)
//                    .build();
//        } else {
//            payMoney.setChargePayMoneyTotal(payMoney.getChargePayMoneyTotal() + 1000000L);
//            payMoney.setPayMoneyBalance(payMoney.getPayMoneyBalance() + 1000000L);
//        }
//
//        payMoney = payMoneyRepository.save(payMoney);
//
//        return TempPayMoneyDto.fromEntity(payMoney);
//    }
//
//}