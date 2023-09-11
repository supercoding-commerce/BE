package com.github.commerce.web.dto.product;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public enum ProductCategoryEnum {
    SWEAT(1,"SWEAT"),
    HOOD(2,"HOOD"),
    KNIT(3,"KNIT"),
    SLEEVELESS(4,"SLEEVELESS"),
    JEANS(5,"JEANS"),
    SHORTS(6,"SHORTS"),
    TRAINING(7,"TRAINING"),
    LEGGINGS(8,"LEGGINGS"),
    SHORTDRESS(9,"SHORTDRESS"),
    LONGDRESS(10,"LONGDRESS"),
    SHOES(11,"SHOES"),
    MUFFLER(12,"MUFFLER"),
    GLOVES(13,"GLOVES"),
    CAP(14,"CAP");
    private final int value;
    private final String label;
    ProductCategoryEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }
    public static String switchCategory(String code) {
        switch(code){
            case "SWEAT": return SWEAT.label;
            case "HOOD": return HOOD.label;
            case "KNIT": return KNIT.label;
            case "SLEEVELESS": return SLEEVELESS.label;
            case "JEANS": return JEANS.label;
            case "SHORTS": return SHORTS.label;
            case "TRAINING": return TRAINING.label;
            case "LEGGINGS": return LEGGINGS.label;
            case "SHORTDRESS": return SHORTDRESS.label;
            case "LONGDRESS": return LONGDRESS.label;
            case "SHOES": return SHOES.label;
            case "MUFFLER": return MUFFLER.label;
            case "GLOVES": return GLOVES.label;
            case "CAP": return CAP.label;
            default: return null;
        }
    }
}
