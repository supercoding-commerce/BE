package com.github.commerce.web.dto.order;

import lombok.*;

import java.util.Map;

public class PostOrderDto {
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        private Long productId;
        private Long cartId;
        private Integer quantity;
        private Map<String, String> options;
    }
}
