package com.github.commerce.web.dto.product;

public enum GenderCategoryEnum {
    male("male"),
    female("female");

    private final String label;

    GenderCategoryEnum(String label) {
        this.label = label;
    }

    public static String switchCategory(String code) {
        switch(code){
            case "male": return male.label;
            case "female": return female.label;
            case "남성": return male.label;
            case "여성": return female.label;
            default : return null;
        }
    }
}
