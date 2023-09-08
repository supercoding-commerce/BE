package com.github.commerce.service.payment;

import com.github.commerce.entity.*;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.repository.payment.PayMoneyRepository;
import com.github.commerce.repository.payment.PaymentRepository;
import com.github.commerce.repository.user.UserRepository;
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
    public PaymentDto purchaseResponse(Long userId, PurchaseDto.Request2 request){

//        1. 주문의 총 금액을 가져온다.
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow();
//        1-1. orderId를 가져와서 totalPrice를 가져옴
        Long orderPrice = order.getTotalPrice();

        //        2. 사용가능한 쿠폰이 있는지 확인한다 (repository를 사용하지 않고 조회)
        User user = userRepository.findById(userId).orElseThrow();

//        2-1. 사용 가능한 쿠폰이 있다면 사용선택 (단, 중복사용 불가능. 결제한건당 한개만 사용가능)
        UsersCoupon usersCoupon = user.setUserCouponIsUsedTrue(request.getCouponId());
        // null이 아닌경우 만 사용가능하게 예외처리 필요

//        2-2. 사용한 쿠폰만큼 주문의 총 금액에서 할인적용
        String discount = usersCoupon.getCoupons().getContent();

        PayMoney payMoney = payMoneyRepository.findTop1ByUsersOrderByIdDesc(user).orElseThrow();
//        1. 주문 총 금액 - 할인금액 = 최종 결제금액
        // false 일때는 - 0 , true 는 내 모든 포인트 소진
        Long point = 0L;
        if (request.getIsUsePoint()){
            point = payMoney.getPointBalance();
            payMoney.setPointBalance(0L);
        }
        Long discountAmount = discountCalculator(discount,orderPrice,point);


//        2. 최종결제금액 ≤ 보유페이머니 결제처리O
         if (discountAmount > payMoney.getPayMoneyBalance()){
            //TODO : 예외처리 필요
        }


//        3. 결제처리 후 결과값들 entity로 저장시켜주기!
        PayMoney newPayMoney = PayMoney.payMoney(payMoney);
         newPayMoney.setUsedChargePayMoney(discountAmount);
         newPayMoney.setPayMoneyBalance(payMoney.getPayMoneyBalance()-discountAmount);
         newPayMoney.setPointBalance(payMoney.getPointBalance());

        payMoneyRepository.save(newPayMoney);

        return PaymentDto.fromEntity(
                paymentRepository.save(
                        Payment.builder()
                                .order(order)
                                .payMoney(payMoney)
                                .paymentMethod(1)
                                .status(1)
                                .build()
                )
        );

    }

    private Long discountCalculator(String discount,Long orderPrice,Long point){

        long discountAmount = 0L;

        if (discount.endsWith("원")){
            discountAmount = Long.parseLong(discount.split("원")[0]);
            discountAmount = orderPrice - discountAmount;
        }else if(discount.endsWith("%")){
            discountAmount = Long.parseLong(discount.split("%")[0]);
            discountAmount = orderPrice * discountAmount / 100;
        }
        return discountAmount - point;
    }



}
