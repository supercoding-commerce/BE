package com.github.commerce.web.dto.cart;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Product;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
    private Long userId;
    private Long cartId;
    private Long productId;
    private String productName;
    private Long price;
    private String imageUrl;
    private Integer quantity;
    private Boolean isOrdered;
    private Integer totalPrice;
    private String options;
    private LocalDateTime createdAt;
    //private Map<String, String> options;

    public static CartDto fromEntity(Cart cart){
        Product product = cart.getProducts();
        return CartDto.builder()
                .userId(cart.getUsers().getId())
                .cartId(cart.getId())
                .productId(product.getId())
                .productName(product.getName())
                .price(product.getPrice())
                .imageUrl(product.getThumbnailUrl())
                //.orderState(OrderStateEnum.getByCode(cart.getOrderState()))
                .isOrdered(cart.getIsOrdered())
                .quantity(cart.getQuantity())
                .totalPrice((int) (product.getPrice() * cart.getQuantity()))
                .options(cart.getOptions())
                .createdAt(cart.getCreatedAt())
                .build();
    }
}
