package com.github.commerce.web.dto.payment;

import com.github.commerce.entity.ChargeHistory;
import com.github.commerce.entity.PayMoney;
import com.github.commerce.entity.Payment;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayMoneyDto {


    @ApiModelProperty(value = "주문내역 식별값")
    private Long paymentId;

    @ApiModelProperty(value = "페이머니 충전내역 식별값")
    private Long chargeHistoryId;

    @ApiModelProperty(value = "충전된 페이머니")
    private Long chargePayMoneyTotal;

    @ApiModelProperty(value = "충전시 결제된 금액")
    private Long paymentAmount;

    @ApiModelProperty(value = "사용한 페이머니")
    private Long usedPayMoney;

    @ApiModelProperty(value = "잔액")
    private Long payMoneyBalance;

    @ApiModelProperty(value = "적립금")
    private Long pointBalance;

    @ApiModelProperty(value = "거래 시간")
    private LocalDateTime createAt;

    public static PayMoneyDto fromEntity(PayMoney payMoney) {
        return Optional.ofNullable(payMoney)
                .map(p -> {
                    Payment payment = p.getPayment();
                    ChargeHistory chargeHistory = p.getChargeHistories();

                    return PayMoneyDto.builder()
                            .paymentId(Optional.ofNullable(payment).map(Payment::getId).orElse(null))
                            .chargeHistoryId(Optional.ofNullable(chargeHistory).map(ChargeHistory::getId).orElse(null))
                            .chargePayMoneyTotal(Optional.ofNullable(chargeHistory).map(ChargeHistory::getPayMoney).orElse(null))
                            .usedPayMoney(p.getUsedChargePayMoney())
                            .payMoneyBalance(p.getPayMoneyBalance())
                            .pointBalance(p.getPointBalance())
                            .createAt(Optional.ofNullable(p.getCreateAt()).map(LocalDateTime::from).orElse(null))
                            .build();
                })
                .orElse(null);
    }
}
