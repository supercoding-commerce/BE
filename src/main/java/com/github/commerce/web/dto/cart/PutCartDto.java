package com.github.commerce.web.dto.cart;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PutCartDto {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PutCartRequest {
        private Long cartId;
        private Long productId;
        private Integer quantity;
        private List<String> options;
    }
}
