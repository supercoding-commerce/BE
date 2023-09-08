package com.github.commerce.web.dto.product;

public enum AgeCategoryEnum {
    십대("10~20"),
    이십대("20~30"),
    삼십대("30~40");
    private final String label;

    AgeCategoryEnum(String label) {
        this.label = label;
    }
}
