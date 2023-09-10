package com.github.commerce.web.dto.order;

import lombok.*;

import java.util.List;
import java.util.Map;

public class PutOrderDto {
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PutOrderRequest {
        private Long orderId;
        private Long productId;
        private Integer quantity;
        private List<String> options;
    }
}
