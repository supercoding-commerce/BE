package com.github.commerce.web.dto.payment;

import com.github.commerce.entity.Payment;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {

    @ApiModelProperty(value = "거래 식별값")
    private Long id;

    @ApiModelProperty(value = "주문리스트 식별값")
    private List<Long> orderIdList;

    @ApiModelProperty(value = "페이머니 식별값")
    private Long payMoneyId;

    @ApiModelProperty(value = "쿠폰 번호")
    private Long couponId;

    @ApiModelProperty(value = "보유 포인트")
    private Long point;

    @ApiModelProperty(value = "보유 페이머니")
    private Long payMoney;

    @ApiModelProperty(value = "최총 결제 금액")
    private Long payMoneyAmount;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "결제 수단")
    private String paymentMethod;

    @ApiModelProperty(value = "남은 페이머니")
    private Long payMoneyBalance;

    @ApiModelProperty(value = "결제 시간")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "결제 상태")
    private String status;

    public static PaymentDto fromEntity(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .payMoneyId(payment.getPayMoney().getId())
                .paymentMethod(PaymentMethodEnum.getByCode(payment.getPaymentMethod()))
                .status(PaymentStatusEnum.getByCode(payment.getStatus()))
                .payMoneyAmount(payment.getPayMoneyAmount())
                .createdAt(payment.getCreatedAt())
                .payMoneyBalance(payment.getPayMoney().getPayMoneyBalance())
                .point(payment.getPayMoney().getPointBalance())
                .build();
    }

}
