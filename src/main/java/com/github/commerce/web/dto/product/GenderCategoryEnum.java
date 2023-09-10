package com.github.commerce.web.dto.product;

public enum GenderCategoryEnum {
    FEMALE(1,"FEMALE"),
    MALE(1,"MALE");

    private final int value;
    private final String label;

    GenderCategoryEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String switchCategory(String code) {
        switch(code){
            case "FEMALE": return FEMALE.label;
            case "MALE": return MALE.label;
            default : return null;
        }
    }
}
