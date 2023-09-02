package com.github.commerce.web.dto.cart;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PostCartDto {
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        private Long productId;
        private Integer quantity;
        private List<Map<String, String>> options;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private Long cartId;
        private Long productId;
        private Integer quantity;
        private Boolean isOrdered;
        private String options;

        public static Response from(CartDto cartDto){
            return Response.builder()
                    .cartId(cartDto.getCartId())
                    .productId(cartDto.getProductId())
                    .quantity(cartDto.getQuantity())
                    .isOrdered(cartDto.getIsOrdered())
                    .options(cartDto.getOptions())
                    .build();
        }
    }
}
