package com.github.commerce.web.dto.order;

import com.github.commerce.entity.Order;
import com.github.commerce.entity.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRmqDto {
    private Long orderId;
    private Long productId;
    private Long userId;
    private Long sellerId;
    private Long cartId;
    private Integer orderState;
    private String orderTag;
    private Integer quantity;
    private Long total_price;
    private String options;

    public static OrderRmqDto fromEntity(Order order){
        Product product = order.getProducts();
        return OrderRmqDto.builder()
                .userId(order.getUsers().getId())
                .productId(product.getId())
                .sellerId(order.getSellers().getId())
                .cartId(order.getCarts().getId())
                .quantity(order.getQuantity())
                .total_price(order.getTotalPrice())
                .orderState(order.getOrderState())
                .orderTag(order.getOrderTag())
                .options(order.getOptions())
                .build();
    }

    public static OrderRmqDto fromEntityForProduct(Order order){
        Product product = order.getProducts();
        return OrderRmqDto.builder()
                .userId(order.getUsers().getId())
                .productId(product.getId())
                .sellerId(order.getSellers().getId())
                .quantity(order.getQuantity())
                .total_price(order.getTotalPrice())
                .orderState(order.getOrderState())
                .orderTag(order.getOrderTag())
                .options(order.getOptions())
                .build();
    }

    public static OrderRmqDto fromEntityForModify(Order order){

        return OrderRmqDto.builder()
                .orderId(order.getId())
                .userId(order.getUsers().getId())
                .productId(order.getProducts().getId())
                .sellerId(order.getSellers().getId())
                .quantity(order.getQuantity())
                .total_price(order.getTotalPrice())
                .orderState(order.getOrderState())
                .options(order.getOptions())
                .build();
    }

}
