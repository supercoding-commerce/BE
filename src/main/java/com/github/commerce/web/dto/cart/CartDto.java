package com.github.commerce.web.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Integer price;
    private String imageUrl;
    private Integer stock;
    private Integer quantity;
    private Boolean isOrdered;
    private String cartState;
    private Integer totalPrice;
    private String options;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
                .stock(product.getLeftAmount())
                .isOrdered(cart.getIsOrdered())
                .cartState(CartStateEnum.getByCode(cart.getCartState()))
                .quantity(cart.getQuantity())
                .totalPrice((int) (product.getPrice() * cart.getQuantity()))
                .options(cart.getOptions())
                .createdAt(cart.getCreatedAt())
                .build();
    }
}
