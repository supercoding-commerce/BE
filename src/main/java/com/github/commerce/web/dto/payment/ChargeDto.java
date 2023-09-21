package com.github.commerce.web.dto.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;


public class ChargeDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChargeRequest {

        @ApiModelProperty(value = "충전할 페이머니")
        private Long payMoney;

        @Min(value = 10000, message = "최소 충전 금액은 10,000원 이상이어야 합니다.")
        @ApiModelProperty(value = "충전시 결제금액")
        private Long paymentAmount;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChargeResponse {

        @ApiModelProperty(value = "충전한 페이머니")
        private Long payMoney;

        @ApiModelProperty(value = "충전시 결제금액")
        private Long paymentAmount;

        @ApiModelProperty(value = "충전 날짜")
        private LocalDateTime chargeDate;

        @Enumerated(EnumType.STRING)
        @ApiModelProperty(value = "결제 상태", example = "1")
        private String status;

        public static ChargeDto.ChargeResponse from(ChargeHistoryDto chargeHistoryDto){
            return ChargeResponse.builder()
                    .payMoney(chargeHistoryDto.getPayMoney())
                    .paymentAmount(chargeHistoryDto.getPaymentAmount())
                    .chargeDate(chargeHistoryDto.getChargeDate())
                    .status(chargeHistoryDto.getStatus())
                    .build();
        }


    }

}
