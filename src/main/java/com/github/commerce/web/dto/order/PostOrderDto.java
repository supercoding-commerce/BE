package com.github.commerce.web.dto.order;

import lombok.*;

import java.util.List;

public class PostOrderDto {
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PostOrderRequest {

        private Long productId;
        private Integer quantity;
        private List<String> options;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PostOrderRequestFromCart {

        private List<Long> cartIdList;

    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PutOrderRequest {

        private Long productId;
        private Long cartId;
        private Integer quantity;
        private List<String> options;
    }
}
