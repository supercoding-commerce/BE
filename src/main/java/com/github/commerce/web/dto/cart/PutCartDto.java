package com.github.commerce.web.dto.cart;

import lombok.*;

import java.util.Map;

@Getter
@Setter
public class PutCartDto {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long cartId;
        private Long productId;
        private Integer quantity;
        private String optionId;
        private Map<String, String> options;
    }
}
