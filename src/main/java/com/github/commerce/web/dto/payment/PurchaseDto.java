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
    public static class Request2{
        // 상품 주문 요청

        @ApiModelProperty(value = "주문 번호")
        private Long orderId;

        @ApiModelProperty(value = "주문 금액")
        private Long orderPrice;

        @ApiModelProperty(value = "쿠폰 번호")
        private Long couponId;

        @ApiModelProperty(value = "포인트 사용여부")
        private Boolean isUsePoint;

        @Enumerated(EnumType.STRING)
        @ApiModelProperty(value = "결제 수단")
        private String paymentMethod;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response{
        // 상품 주문 응답
        @ApiModelProperty(value = "페이머니 번호")
        private Long payMoneyId;

        @ApiModelProperty(value = "총 결제 금액")
        private Long totalPrice;

        @ApiModelProperty(value = "남은 페이 머니")
        private Long payMoneyBalance;

        @ApiModelProperty(value = "결제 시간")
        private LocalDateTime createdAt;

        public static Response from(PaymentDto paymentDto){
            return Response.builder()
                    .payMoneyId(paymentDto.getPayMoneyId())
                    .totalPrice(paymentDto.getTotalPrice())
                    .payMoneyBalance(paymentDto.getPayMoneyBalance())
                    .createdAt(paymentDto.getCreatedAt())
                    .build();
        }
    }

}
