package com.github.commerce.web.dto.order;

import com.github.commerce.entity.Order;
import com.github.commerce.entity.Product;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
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
    private Integer total_price;
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
                .total_price(order.getTotal_price())
                .orderState(OrderStateEnum.getByCode(order.getOrderState()))
                .options(order.getOptions())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
