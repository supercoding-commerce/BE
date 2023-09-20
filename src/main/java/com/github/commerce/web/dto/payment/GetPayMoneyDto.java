package com.github.commerce.web.dto.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class GetPayMoneyDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetPayMoneyResponse{

        @ApiModelProperty(value = "사용 금액")
        private Long usedPayMoney;

        @ApiModelProperty(value = "충전 금액")
        private Long chargePayMoney;

        @ApiModelProperty(value = "보유 포인트")
        private Long point;

        @ApiModelProperty(value = "잔여 페이머니")
        private Long payMoneyBalance;

        @ApiModelProperty(value = "생성 시간")
        private LocalDateTime createAt;

        public static GetPayMoneyResponse from(PayMoneyDto payMoneyDto){
            return GetPayMoneyResponse.builder()
                    .usedPayMoney(payMoneyDto.getUsedPayMoney())
                    .chargePayMoney(payMoneyDto.getChargePayMoneyTotal())
                    .point(payMoneyDto.getPointBalance())
                    .payMoneyBalance(payMoneyDto.getPayMoneyBalance())
                    .createAt(payMoneyDto.getCreateAt())
                    .build();
        }

    }
}
