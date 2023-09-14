package com.github.commerce.web.dto.payment;

import com.github.commerce.entity.Payment;
import com.github.commerce.entity.UsersCoupon;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {

    @ApiModelProperty(value = "주문 번호")
    private Long orderId;

    @ApiModelProperty(value = "페이머니 번호")
    private Long payMoneyId;

    @ApiModelProperty(value = "주문 금액")
    private Long orderPrice;

    @ApiModelProperty(value = "쿠폰 번호")
    private Long couponId;

    @ApiModelProperty(value = "보유 포인트")
    private Long point;

    @ApiModelProperty(value = "보유 페이머니")
    private Long payMoney;

    @ApiModelProperty(value = "총 결제 금액")
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "결제 수단")
    private String paymentMethod;

    @ApiModelProperty(value = "남은 페이머니")
    private Long payMoneyBalance;

    @ApiModelProperty(value = "결제 시간")
    private LocalDateTime createdAt;

    public static PaymentDto fromEntity(Payment payment) {

        return PaymentDto.builder()
                .orderId(payment.getOrder().getId())
                .orderPrice(payment.getOrder().getTotalPrice())
//                .couponId(usersCoupon.getId())
                .point(payment.getPayMoney().getPointBalance() != null ? payment.getPayMoney().getPointBalance() : 0L)
                .payMoney(payment.getPayMoney().getPayMoneyBalance())
                .totalPrice(payment.getPayMoney().getUsedChargePayMoney())
                .paymentMethod(PaymentMethodEnum.getByCode(payment.getPaymentMethod()))
                .payMoneyBalance(payment.getPayMoney().getPayMoneyBalance())
                .createdAt(LocalDateTime.from(payment.getCreateAt()))
                .build();
    }
}
