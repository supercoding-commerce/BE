package com.github.commerce.web.dto.product;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public enum ProductCategoryEnum {
    상의("상의"),
    하의("하의"),
    신발("신발"),
    외투("외투"),
    기타("기타");

    private final String label;

}
