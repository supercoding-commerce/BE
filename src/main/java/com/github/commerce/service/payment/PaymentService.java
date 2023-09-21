package com.github.commerce.service.payment;

import com.github.commerce.entity.*;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.repository.payment.PayMoneyRepository;
import com.github.commerce.repository.payment.PaymentRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.coupon.UserCouponService;
import com.github.commerce.service.coupon.exception.CouponException;
import com.github.commerce.service.payment.exception.PaymentErrorCode;
import com.github.commerce.service.payment.exception.PaymentException;
import com.github.commerce.web.dto.coupon.UsersCouponResponseDto;
import com.github.commerce.web.dto.payment.PaymentDto;
import com.github.commerce.web.dto.payment.PurchaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final PayMoneyRepository payMoneyRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final UserCouponService userCouponService;

    @Transactional
    public PaymentDto purchaseOrder(Long userId, PurchaseDto.PurchaseRequest request) {

        Long totalPaymentPrice = request.getTotalPrice();

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_USER_NOT_FOUND));

        // 쿠폰 사용 여부 확인
        if (request.getCouponId() != null && request.getCouponId() > 0) {
            try {
                userCouponService.usedUserCoupon(userId, request.getCouponId());
            } catch (CouponException e) {
                // 쿠폰 사용 실패 예외 처리
                throw new PaymentException(PaymentErrorCode.PAYMENT_INVALID_COUPON);
            }
        }

        // 포인트 초기화
        Long point = 0L;

        // 포인트를 사용하지 않는 경우에는 기존 포인트 잔액을 그대로 유지
        if (!request.getIsUsePoint()) {
            point = payMoneyRepository.findTop1ByUsersOrderByIdDesc(user)
                    .map(PayMoney::getPointBalance)
                    .orElse(0L);
        }

        // 페이머니 조회
        PayMoney payMoney = payMoneyRepository.findTop1ByUsersOrderByIdDesc(user)
                .orElseThrow(() -> new RuntimeException("페이머니를 찾을 수 없습니다."));

        // 결제 가능 여부 확인
        if (totalPaymentPrice > payMoney.getPayMoneyBalance()) {
            throw new PaymentException(PaymentErrorCode.PAYMENT_INSUFFICIENT_BALANCE);
        }

        // 페이머니 업데이트
        PayMoney newPayMoney = PayMoney.usePayMoney(payMoney);
        newPayMoney.setUsedChargePayMoney(totalPaymentPrice);
        newPayMoney.setPayMoneyBalance(payMoney.getPayMoneyBalance() - totalPaymentPrice);
        newPayMoney.setPointBalance(point);

        if (request.getIsUsePoint()) {
            newPayMoney.setPointBalance(0L);
        }

        // 결제 정보 생성
        Payment payment = Payment.builder()
                .payMoney(newPayMoney)
                .paymentMethod(Integer.valueOf(request.getPaymentMethod()))
                .payMoneyAmount(totalPaymentPrice)
                .status(Integer.valueOf(request.getPaymentMethod()))
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        updateOrderStatus(request.getOrderIdList());

        // PayMoney 엔티티 업데이트
        payMoneyRepository.save(newPayMoney);

        // 결제 정보 반환
        return PaymentDto.fromEntity(savedPayment);
    }

    private void updateOrderStatus(List<Long> orderIds) {
        for (Long orderId : orderIds) {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_INVALID_ORDER));


            if (order.getOrderState() == 2) {
                throw new PaymentException(PaymentErrorCode.PAYMENT_ORDER_ALREADY_COMPLETED);
            }

            if(order.getCarts().getId() != null){
                updateCartState(order.getCarts().getId());
            }

            int orderStateCode = 2;
            order.setOrderState(orderStateCode);
            orderRepository.save(order);
        }
    }

    private void updateCartState(Long cartId) {
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_INVALID_ORDER));

            cart.setIsOrdered(true);
            int cartStateCode = 2;
            cart.setCartState(cartStateCode);
            cartRepository.save(cart);
    }


}

