package com.github.commerce.web.dto.payment;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

public class PurchaseDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseRequest {

        @ApiModelProperty(value = "주문 번호")
        private Long orderId;

        @ApiModelProperty(value = "쿠폰 번호")
        private Long couponId;

        @ApiModelProperty(value = "포인트 사용여부", example = "false")
        private Boolean isUsePoint = false;

        @Enumerated(EnumType.STRING)
        @ApiModelProperty(value = "결제 수단", example = "1")
        private String paymentMethod;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PurchaseResponse {

        @ApiModelProperty(value = "주문 번호")
        private Long orderId;

        @ApiModelProperty(value = "총 결제 금액")
        private Long totalPrice;

        @ApiModelProperty(value = "남은 페이 머니")
        private Long payMoneyBalance;

        @ApiModelProperty(value = "결제 시간")
        private LocalDateTime createdAt;

        @Enumerated(EnumType.STRING)
        @ApiModelProperty(value = "결제 수단", example = "1")
        private String paymentMethod;

        public static PurchaseResponse from(PaymentDto paymentDto){
            return PurchaseDto.PurchaseResponse.builder()
                    .orderId(paymentDto.getOrderId())
                    .totalPrice(paymentDto.getTotalPrice())
                    .payMoneyBalance(paymentDto.getPayMoneyBalance())
                    .paymentMethod(paymentDto.getPaymentMethod())
                    .createdAt(paymentDto.getCreatedAt())
                    .build();
        }
    }

}
