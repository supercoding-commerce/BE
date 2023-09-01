package com.github.commerce.service.order.exception;

import com.github.commerce.service.cart.exception.CartErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderErrorResponse {

    private OrderErrorCode errorCode;
    private String errorMessage;
}

