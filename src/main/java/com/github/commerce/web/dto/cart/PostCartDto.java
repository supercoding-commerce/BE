package com.github.commerce.web.dto.cart;

import lombok.*;
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
        private Map<String, String> options;
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
        private String orderState;
        private String optionId;
        private Map<String, String> options;

        public static Response from(CartDto cartDto){
            return Response.builder()
                    .cartId(cartDto.getCartId())
                    .productId(cartDto.getProductId())
                    .quantity(cartDto.getQuantity())
                    .orderState(cartDto.getOrderState())
                    .optionId(cartDto.getOptionId())
                    .options(cartDto.getOptions())
                    .build();
        }
    }
}
