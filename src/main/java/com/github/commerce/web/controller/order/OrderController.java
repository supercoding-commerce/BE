package com.github.commerce.web.controller.order;

import com.github.commerce.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/order")
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity createOrder(){
        Long userId = 1L;
        return null;
    }



}
