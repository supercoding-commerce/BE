package com.github.commerce.web.dto.payment;

import com.github.commerce.entity.PayMoney;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    public static PayMoneyDto fromEntity(PayMoney payMoney){

        return PayMoneyDto.builder()
                .paymentId(payMoney.getPayment().getId())
                .chargeHistoryId(payMoney.getChargeHistories().getId())
                .chargePayMoneyTotal(payMoney.getChargeHistories().getPayMoney())
                .usedPayMoney(payMoney.getUsedChargePayMoney())
                .payMoneyBalance(payMoney.getPayMoneyBalance())
                .pointBalance(payMoney.getPointBalance())
                .createAt(LocalDateTime.from(payMoney.getCreateAt()))
                .build();

    }

}
