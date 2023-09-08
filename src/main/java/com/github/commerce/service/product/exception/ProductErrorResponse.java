package com.github.commerce.service.product.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductErrorResponse {
    private ProductErrorCode errorCode;
    private String errorMessage;
}
