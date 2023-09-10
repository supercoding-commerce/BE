package com.github.commerce.web.dto.product;

public enum AgeCategoryEnum {
    십대(1,"10"),
    이십대(2,"20"),
    삼십대(3,"30");
    private final int value;
    private final String label;

    AgeCategoryEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String switchCategory(String code) {
        switch(code){
            case "10": return 십대.label;
            case "20": return 이십대.label;
            case "30": return 삼십대.label;
            default: return null;
        }
    }
}
