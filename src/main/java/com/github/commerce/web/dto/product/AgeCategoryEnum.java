package com.github.commerce.web.dto.product;

public enum AgeCategoryEnum {
    십대(1,"10~20"),
    이십대(2,"20~30"),
    삼십대(3,"30~40"),
    기타(4,"etc");
    private final int value;
    private final String label;

    AgeCategoryEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String switchCategory(int code) {
        switch(code){
            case 1: return 십대.label;
            case 2: return 이십대.label;
            case 3: return 삼십대.label;
            case 4: return 기타.label;
            default: return 기타.label;
        }
    }
}
