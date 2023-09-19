package com.github.commerce.web.dto.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;

public class PurchaseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseRequest {

        @ApiModelProperty(value = "주문 번호")
        private List<Long> orderIdList;

        @ApiModelProperty(value = "쿠폰 번호")
        private Long couponId;

        @ApiModelProperty(value = "총 결제 금액")
        private Long totalPrice;

        @ApiModelProperty(value = "포인트 사용여부", example = "false")
        private Boolean isUsePoint = false;

        @Enumerated(EnumType.STRING)
        @ApiModelProperty(value = "결제 수단", example = "1")
        private String paymentMethod;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseResponse {

        @ApiModelProperty(value = "거래 식별값")
        private Long paymentId;

        @ApiModelProperty(value = "지갑 식별값")
        private Long payMoneyId;

        @ApiModelProperty(value = "최종 결제 금액")
        private Long payMoneyAmount;

        @ApiModelProperty(value = "남은 페이 머니")
        private Long payMoneyBalance;

        @ApiModelProperty(value = "결제 시간")
        private LocalDateTime createdAt;

        @Enumerated(EnumType.STRING)
        @ApiModelProperty(value = "결제 수단", example = "1")
        private String paymentMethod;

        @Enumerated(EnumType.STRING)
        @ApiModelProperty(value = "결제 상태", example = "1")
        private String status;

        public static PurchaseResponse from(PaymentDto paymentDto){
            return PurchaseResponse.builder()
                    .paymentId(paymentDto.getId())
                    .payMoneyId(paymentDto.getPayMoneyId())
                    .payMoneyAmount(paymentDto.getPayMoneyAmount())
                    .payMoneyBalance(paymentDto.getPayMoneyBalance())
                    .paymentMethod(paymentDto.getPaymentMethod())
                    .status(paymentDto.getStatus())
                    .createdAt(paymentDto.getCreatedAt())
                    .build();
        }

    }

}
