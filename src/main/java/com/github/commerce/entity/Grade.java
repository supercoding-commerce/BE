package com.github.commerce.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Grade {
    ALL(0),
    GREEN(1),
    ORANGE(2),
    RED(3),
    VIP(4),
    ADMIN(5);

    private int priority;
}
