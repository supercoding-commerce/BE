package com.github.commerce.web.dto.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class DelayedOrderDto {
    private Long userId;
    private Long productId;
    private Integer quantity;
    private List<Map<String, String>> options;
}
