package com.github.commerce.web.dto.payment;


public enum PointStatusEnum {

    EARN_POINT(1, "적립"),
    USE_POINT(2, "사용");

    private final int value;
    private final String label;

    PointStatusEnum(int value, String label){
        this.value = value;
        this.label = label;
    }

    public static String getByCode(int code){
        switch(code){

            case 1: return EARN_POINT.label;
            case 2: return USE_POINT.label;

            default: return EARN_POINT.label;
        }
    }
}
