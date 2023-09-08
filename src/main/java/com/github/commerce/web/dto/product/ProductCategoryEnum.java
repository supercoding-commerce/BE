package com.github.commerce.web.dto.product;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public enum ProductCategoryEnum {
    상의(1,"상의"),
    하의(2,"하의"),
    신발(3,"신발"),
    외투(4,"외투"),
    기타(5,"기타");
    private final int value;
    private final String label;
    ProductCategoryEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }
    public static String switchCategory(int code) {
        switch(code){
            case 1: return 상의.label;
            case 2: return 하의.label;
            case 3: return 신발.label;
            case 4: return 외투.label;
            case 5: return 기타.label;
            default: return 기타.label;
        }
    }
}
