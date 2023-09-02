package com.github.commerce.web.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class DelayedOrderDto {
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Map<String, String> options;

}
