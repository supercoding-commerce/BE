package com.github.commerce.web.dto.payment;

import com.github.commerce.entity.PayMoney;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayMoneyDto {

    @ApiModelProperty(value = "페이머니 충전내역")
    private Long chargeHistoryId;

    @ApiModelProperty(value = "충전된 페이머니")
    private Long chargePayMoneyTotal;

    @ApiModelProperty(value = "사용한 페이머니")
    private Long usedChargePayMoney;

    @ApiModelProperty(value = "잔액")
    private Long payMoneyBalance;

    @ApiModelProperty(value = "적립금")
    private Long pointBalance;

    @ApiModelProperty(value = "거래 시간")
    private LocalDateTime createAt;

    @ApiModelProperty(value = "PG결제 ID")
    private String pgPaymentId;

//    public static PayMoneyDto fromEntity(PayMoney payMoney){
//
//
//    }

}
