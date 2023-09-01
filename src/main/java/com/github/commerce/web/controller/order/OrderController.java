package com.github.commerce.web.controller.order;

import com.github.commerce.service.order.OrderService;
import com.github.commerce.web.dto.order.OrderDto;
import com.github.commerce.web.dto.order.PostOrderDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/order")
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @RequestBody PostOrderDto.Request request
            ){
        Long userId = 1L;
        return ResponseEntity.ok(orderService.createOrder(request, userId));
    }

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getOrderList(
            @RequestParam(defaultValue = "0") Long cursorId
    ){
        Long userId = 1L;
        return ResponseEntity.ok(orderService.getOrderList(userId, cursorId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOne(
            @PathVariable Long orderId
    ){
        Long userId = 1L;
        return ResponseEntity.ok(orderService.deleteOne(orderId, userId));
    }



}
