package com.github.commerce.web.dto.order;

import lombok.*;

import java.util.List;
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
        private List<Map<String, String>> options;
    }
}
