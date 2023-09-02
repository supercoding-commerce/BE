package com.github.commerce.web.dto.cart;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRmqDto {
    private User user;
    private Long productId;
    private String productName;
    private Long price;
    private String imageUrl;
    private Integer quantity;
    private Boolean isOrdered;
    private Integer totalPrice;
    private String options;
    private LocalDateTime createdAt;

    public static CartRmqDto fromEntity(Cart cart){
        return CartRmqDto.builder()
                .user(cart.getUsers())
                .productId(cart.getProducts().getId())
                .isOrdered(cart.getIsOrdered())
                .quantity(cart.getQuantity())
                .options(cart.getOptions())
                .build();
    }
}
