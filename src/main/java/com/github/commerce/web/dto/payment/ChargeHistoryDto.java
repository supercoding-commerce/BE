package com.github.commerce.web.dto.payment;

import com.github.commerce.entity.ChargeHistory;
import com.github.commerce.entity.Payment;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargeHistoryDto {

    @ApiModelProperty(value = "충전 페이머니")
    private Long payMoney;

    @ApiModelProperty(value = "충전시 결제금액")
    private Long paymentAmount;

    @ApiModelProperty(value = "충전 날짜")
    private LocalDateTime chargeDate;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "결제 상태")
    private String status;

    public static ChargeHistoryDto from(ChargeHistory chargeHistory){
        return ChargeHistoryDto.builder()
                .payMoney(chargeHistory.getPayMoney())
                .paymentAmount(chargeHistory.getPaymentAmount())
                .chargeDate(chargeHistory.getChargeDate())
                .status(PaymentStatusEnum.getByCode(chargeHistory.getStatus() != null ? chargeHistory.getStatus() : 1))
                .build();
    }
}
