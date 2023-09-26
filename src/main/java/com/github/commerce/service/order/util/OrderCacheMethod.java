package com.github.commerce.service.order.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderCacheMethod {
    //userId, orderTag
    private final Map<Long, String> orderTagStorage = new ConcurrentHashMap<>();

    public void putOrderTag(Long userId, String orderTag) {
        orderTagStorage.put(userId, orderTag);
    }

    public String getOrderTag(Long userId) {
        return orderTagStorage.get(userId);
    }

}
