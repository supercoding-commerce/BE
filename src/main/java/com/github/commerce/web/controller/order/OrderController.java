package com.github.commerce.web.controller.order;

import com.github.commerce.service.order.OrderService;
import com.github.commerce.web.dto.order.GetOrderDto;
import com.github.commerce.web.dto.order.OrderDto;
import com.github.commerce.web.dto.order.PostOrderDto;
import com.github.commerce.web.dto.order.PutOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/order")
@RestController
public class OrderController {
    private final OrderService orderService;

    /**
     * 주문 생성
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<String> createOrder(
            @RequestBody PostOrderDto.Request request
            ){
        Long userId = 1L;
        return ResponseEntity.ok(orderService.createOrder(request, userId));
    }

    /**
     * 회원의 주문내역 전체 조회
     * @param cursorId
     * @return
     */
    @GetMapping
    public ResponseEntity getOrderList(
            @RequestParam(required = false) Long cursorId
    ){
        Long userId = 1L;

        if(cursorId == null){
            return ResponseEntity.ok(
                    orderService.getOrderList(userId)
            );
        }else{
            return ResponseEntity.ok(GetOrderDto.Response.fromPage(
                    orderService.getOrderListByCursor(userId, cursorId))
            );
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrder(
            @PathVariable Long orderId
    ){
        Long userId = 1L;
        return ResponseEntity.ok(
                orderService.getOrder(orderId, userId)
        );
    }

    /**
     * 주문 수정
     * @param request
     * @return
     */
    @PutMapping
    public ResponseEntity<String> modify(
            @RequestBody PutOrderDto.Request request
    ){
        Long userId = 1L;
        return ResponseEntity.ok(orderService.modifyOrder(request, userId));
    }

    /**
     * 주문 개별삭제
     * @param orderId
     * @return
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOne(
            @PathVariable Long orderId
    ){
        Long userId = 1L;
        return ResponseEntity.ok(orderService.deleteOne(orderId, userId));
    }



}
