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
public class TempPayMoneyDto {

    @ApiModelProperty(value = "잔액")
    private Long payMoneyBalance;

    @ApiModelProperty(value = "거래 시간")
    private LocalDateTime createAt;


    public static TempPayMoneyDto fromEntity(PayMoney payMoney) {

        return TempPayMoneyDto.builder()
                .payMoneyBalance(payMoney.getPayMoneyBalance())
                .createAt(LocalDateTime.from(payMoney.getCreatedAt()))
                .build();

    }

}
