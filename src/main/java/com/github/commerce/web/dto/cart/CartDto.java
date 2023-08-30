package com.github.commerce.web.dto.cart;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.mongocollection.CartSavedOption;
import lombok.*;

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
    private Integer quantity;
    private String orderState;
    private Integer totalPrice;
    private Map<String, String> options;

    public static CartDto fromEntity(Cart cart, Map<String, String> options){
        //Product product = cart.getProduct();
        return CartDto.builder()
                //.userId(cart.getUser().getId())
                .userId(1L)
                .cartId(cart.getCartId())
                //.productId(.getId())
                .productId(1L)
                //.productName(product.getProductName())
                .productName("test")
                //.price(product.getPrice())
                .price(1000)
                //.imageUrl(product.getImageUrl())
                .imageUrl("test")
                .orderState(OrderStateEnum.getByCode(cart.getOrderState()))
                .quantity(cart.getQuantity())
                .totalPrice(1000 * cart.getQuantity())
                .options(options)
                .build();
    }
}
