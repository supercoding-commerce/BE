package com.github.commerce.service.payment;

import com.github.commerce.entity.*;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.repository.payment.PayMoneyRepository;
import com.github.commerce.repository.payment.PaymentRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.payment.exception.PaymentErrorCode;
import com.github.commerce.service.payment.exception.PaymentException;
import com.github.commerce.web.dto.payment.PaymentDto;
import com.github.commerce.web.dto.payment.PurchaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PayMoneyRepository payMoneyRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Transactional
    public PaymentDto purchaseOrder(Long userId, PurchaseDto.PurchaseRequest request) {

        // 1. 주문 조회
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_INVALID_ORDER));

        Long orderPrice = order.getTotalPrice();

        // 2. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_USER_NOT_FOUND));

        UsersCoupon usersCoupon = null;

        // 3. 쿠폰 사용 여부 확인
        if (request.getCouponId() != null && request.getCouponId() > 0) {
            usersCoupon = user.setUserCouponIsUsedTrue(request.getCouponId());
            if (usersCoupon == null) {
                throw new PaymentException(PaymentErrorCode.PAYMENT_INVALID_COUPON);
            }
        }

        // 4. 할인 계산
        String discount = usersCoupon != null ? usersCoupon.getCoupons().getContent() : "0원";

        // 5. 포인트 초기화
        Long point = 0L;

        // 포인트를 사용하지 않는 경우에는 기존 포인트 잔액을 그대로 유지
        if (!request.getIsUsePoint()) {
            point = payMoneyRepository.findTop1ByUsersOrderByIdDesc(user)
                    .map(PayMoney::getPointBalance)
                    .orElse(0L);
        }

        // 6. 할인 계산
        Long discountPrice = discountCalculator(discount, orderPrice, point, request);

        // 7. 페이머니 조회
        PayMoney payMoney = payMoneyRepository.findTop1ByUsersOrderByIdDesc(user)
                .orElseThrow(() -> new RuntimeException("페이머니를 찾을 수 없습니다."));

        // 8. 결제 가능 여부 확인
        if (discountPrice > payMoney.getPayMoneyBalance()) {
            throw new PaymentException(PaymentErrorCode.PAYMENT_INSUFFICIENT_BALANCE);
        }

        // 9. 페이머니 업데이트
        PayMoney newPayMoney = PayMoney.payMoney(payMoney);
        newPayMoney.setUsedChargePayMoney(discountPrice);
        newPayMoney.setPayMoneyBalance(payMoney.getPayMoneyBalance() - discountPrice);
        newPayMoney.setPointBalance(point);

        if (request.getIsUsePoint()){
            newPayMoney.setPointBalance(0L);
        }

        payMoneyRepository.save(newPayMoney);

        // 10. 결제 정보 생성
        Payment payment = Payment.builder()
                .order(order)
                .payMoney(newPayMoney)
                .paymentMethod(Integer.valueOf(request.getPaymentMethod()))
                .status(1)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // 11. 주문 상태 업데이트
        int orderStateCode = 2;
        order.setOrderState(orderStateCode);
        orderRepository.save(order);

        // 12. 결제 정보 반환
        return PaymentDto.fromEntity(savedPayment);
    }

    private Long discountCalculator(String discount, Long orderPrice, Long point, PurchaseDto.PurchaseRequest request) {

        long discountAmount = 0L;

        if (discount.endsWith("원")) {
            discountAmount = Long.parseLong(discount.split("원")[0]);
        } else if (discount.endsWith("%")) {
            discountAmount = Long.parseLong(discount.split("%")[0]);
            discountAmount = orderPrice * discountAmount / 100;
        }
        discountAmount = orderPrice - discountAmount;

        // 포인트 할인 계산
        if (request.getIsUsePoint()) {
            // 포인트를 사용하는 경우
            discountAmount = discountAmount - point;
        }

        return discountAmount;
    }
}