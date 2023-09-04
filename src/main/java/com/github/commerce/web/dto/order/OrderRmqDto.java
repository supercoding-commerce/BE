package com.github.commerce.web.dto.order;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Order;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRmqDto {
    private Long orderId;
    private Long productId;
    private Long cartId;
    private Long userId;
    private Integer orderState;
    private Integer quantity;
    private Integer total_price;
    private String options;

    public static OrderRmqDto fromEntity(Order order){
        Product product = order.getProducts();
        Long cartId = order.getCarts() != null ? order.getCarts().getId() : null;
        return OrderRmqDto.builder()
                .cartId(cartId)
                .userId(order.getUsers().getId())
                .productId(product.getId())
                .quantity(order.getQuantity())
                .total_price(order.getTotal_price())
                .orderState(order.getOrderState())
                .options(order.getOptions())
                .build();
    }

    public static OrderRmqDto fromEntityForModify(Order order){

        return OrderRmqDto.builder()
                .orderId(order.getId())
                .userId(order.getUsers().getId())
                .productId(order.getProducts().getId())
                .quantity(order.getQuantity())
                .total_price(order.getTotal_price())
                .orderState(order.getOrderState())
                .options(order.getOptions())
                .build();
    }

}
