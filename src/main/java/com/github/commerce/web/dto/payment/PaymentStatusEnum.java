package com.github.commerce.web.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

public enum PaymentStatusEnum {

    SUCCESS(1, "정상"),
    CANCEL(2, "취소"),
    REFUND(3, "환불"),
    ERROR(4, "오류");

    private final int value;
    private final String label;

    PaymentStatusEnum(int value, String label){
        this.value = value;
        this.label = label;
    }

    public static String getByCode(int code){
        switch(code){

            case 1: return SUCCESS.label;
            case 2: return CANCEL.label;
            case 3: return REFUND.label;
            case 4: return ERROR.label;

            default: return SUCCESS.label;
        }
    }
}
