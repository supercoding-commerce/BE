package com.github.commerce.service.shop.exception;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopErrorResponse {
    private ShopErrorCode errorCode;
    private String errorMessage;
}
