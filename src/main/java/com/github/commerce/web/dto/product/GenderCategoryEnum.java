package com.github.commerce.web.dto.product;

public enum GenderCategoryEnum {
    female(1,"female"),
    male(1,"male");

    private final int value;
    private final String label;

    GenderCategoryEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String switchCategory(int code) {
        switch(code){
            case 1: return female.label;
            case 2: return male.label;
            default : return female.label;
        }
    }
}
