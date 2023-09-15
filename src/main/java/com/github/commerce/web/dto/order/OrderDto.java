package com.github.commerce.web.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Integer price;
    private String productName;
    private String imageUrl;
    private Long cartId;
    private String orderState;
    private Integer quantity;
    private Long totalPrice;
    private String options;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static OrderDto fromEntity(Order order){
        Product product = order.getProducts();
        return OrderDto.builder()
                .orderId(order.getId())
                .productName(product.getName())
                .imageUrl(product.getThumbnailUrl())
                .price(product.getPrice())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .orderState(OrderStateEnum.getByCode(order.getOrderState()))
                .options(order.getOptions())
                .createdAt(order.getCreatedAt())
                .build();
    }


}
