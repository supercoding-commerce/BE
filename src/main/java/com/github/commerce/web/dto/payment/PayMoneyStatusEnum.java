package com.github.commerce.web.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayMoneyStatusEnum {
    SUCCESS(1, "성공"),
    CANCEL(2, "취소"),
    REFUND(3, "환불"),
    ERROR(4, "오류");

    private final int value;
    private final String label;
}
