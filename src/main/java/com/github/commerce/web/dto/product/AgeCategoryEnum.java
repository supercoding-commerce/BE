package com.github.commerce.web.dto.product;

public enum AgeCategoryEnum {
    십대("10~20"),
    이십대("20~30"),
    삼십대("30~40"),
    기타("etc");
    private final String label;

    AgeCategoryEnum(String label) {
        this.label = label;
    }

    public static String switchCategory(String code) {
        switch(code){
            case "십대": return 십대.label;
            case "이십대": return 이십대.label;
            case "삼십대": return 삼십대.label;
            case "기타": return 기타.label;
            default: return null;
        }
    }
}
