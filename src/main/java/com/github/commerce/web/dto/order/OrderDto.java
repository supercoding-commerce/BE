package com.github.commerce.web.dto.order;

import com.github.commerce.entity.Order;
import com.github.commerce.entity.Product;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long orderId;
    private Long productId;
    private Long cartId;
    private String orderState;
    private Integer quantity;
    private Long totalPrice;
    private String options;
    LocalDateTime createdAt;

    public static OrderDto fromEntity(Order order){
        Product product = order.getProducts();
        //Cart cart = order.getCarts();
        return OrderDto.builder()
                .orderId(order.getId())
                //.cartId(cart.getId())
                .productId(product.getId())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .orderState(OrderStateEnum.getByCode(order.getOrderState()))
                .options(order.getOptions())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
