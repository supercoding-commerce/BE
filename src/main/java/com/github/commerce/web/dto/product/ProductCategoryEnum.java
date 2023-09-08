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
    public static String switchCategory(String code) {
        switch(code){
            case "상의": return 상의.label;
            case "하의": return 하의.label;
            case "신발": return 신발.label;
            case "외투": return 외투.label;
            case "기타": return 기타.label;
            //case 7: return ETC.name;
            default: return null;
        }
    }
}
