package com.github.commerce.web.dto.payment;

import com.github.commerce.entity.Payment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargeHistoryDto {

    private Long paymentsId;

    private Long payMoney;

    private Long paymentAmount;

    private Long bonusPayMoney;

    private Long totalPayMoney;

    private LocalDateTime chargeDate;

    private Integer status;
}
