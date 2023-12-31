package com.github.commerce.web.dto.order;

import com.github.commerce.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DetailPageOrderDto {

        private Long orderId;
        private Boolean isReviewed;
        private String orderOption;

        public static DetailPageOrderDto fromEntity(Order order){
            return DetailPageOrderDto.builder()
                    .orderId(order.getId())
                    .isReviewed(order.getIsReviewed())
                    .orderOption(order.getOptions())
                    .build();
        }


}
