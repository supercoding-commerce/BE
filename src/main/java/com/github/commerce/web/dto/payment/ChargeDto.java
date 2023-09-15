package com.github.commerce.web.dto.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;


public class ChargeDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChargeRequest {

        @ApiModelProperty(value = "주문 번호")
        private Long orderId;

        @ApiModelProperty(value = "쿠폰 번호")
        private Long couponId;

        @Enumerated(EnumType.STRING)
        @ApiModelProperty(value = "결제 수단", example = "1")
        private String paymentMethod;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChargeResponse {

        @ApiModelProperty(value = "주문 번호")
        private Long orderId;

        @ApiModelProperty(value = "충전 페이머니")
        private Long chargePayMoney;

        @ApiModelProperty(value = "충전시 결제된 금액")
        private Long paymentAmount;

        @ApiModelProperty(value = "남은 페이 머니")
        private Long payMoneyBalance;

        @ApiModelProperty(value = "결제 시간")
        private LocalDateTime createdAt;

        @Enumerated(EnumType.STRING)
        @ApiModelProperty(value = "결제 수단", example = "1")
        private String paymentMethod;

        public static ChargeDto.ChargeResponse from(PayMoneyDto payMoneyDto){
            return ChargeResponse.builder()
//                    .chargePayMoney()
                    .build();
        }

//        public static PayMoney from(Long id, ChargeRequest request) {
//            return ;
//        }
    }

}
