package com.github.commerce.web.controller.order;

import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.order.OrderService;
import com.github.commerce.web.dto.order.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Api(tags = "물품주문 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/order")
@RestController
public class  OrderController {
    private final OrderService orderService;

    /**
     * 상품페이지로부터 바로 주문
     * @param
     * @return
     */
    @ApiOperation(value = "상품상세페이지로부터 주문 등록, 로그인필요")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping
    public ResponseEntity<List<String>> createOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody List<PostOrderDto.PostOrderRequest> postOrderRequestList
            ){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(orderService.createOrder(postOrderRequestList, userId));
    }

    /**
     * 장바구니로부터 주문 생성
     * @param
     * @return
     */
    @ApiOperation(value = "장바구니로부터 주문 등록, 로그인필요")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping("/cart")
    public ResponseEntity<List<String>> createOrderFromCart(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            //@PathVariable Long cartId
            @RequestBody PostOrderDto.PostOrderRequestFromCart request
    ){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(orderService.createOrderFromCart(request.getCartIdList(), userId));
    }

    /**
     * 회원의 주문내역 전체 조회
     * @param cursorId
     * @return
     */
    @ApiOperation(value = "사용자 주문내역 조회, 로그인필요, cursorId는 없어도 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = List.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrderList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false) Long cursorId
    ){
        Long userId = userDetails.getUser().getId();

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

//    /**
//     * 해당 장바구니를 통한 주문들 조회
//     * @param
//     * @return
//     */
//    @ApiOperation(value = "해당 장바구니를 통한 주문들 조회")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Success", response = List.class),
//            @ApiResponse(code = 400, message = "Bad Request")
//    })
//    @GetMapping("/cart/{orderTag}")
//    public ResponseEntity<List<OrderDto>> getOrderListFromCart(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @PathVariable String orderTag
//    ){
//        Long userId = userDetails.getUser().getId();
//
//            return ResponseEntity.ok(
//                    orderService.getOrderListFromCart(userId, orderTag)
//            );
//
//    }

//    /**
//     * 유저의 해당 상품의 주문들 조회
//     * @param
//     * @return
//     */
//    @ApiOperation(value = "해당 장바구니를 통한 주문들 조회")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Success", response = List.class),
//            @ApiResponse(code = 400, message = "Bad Request")
//    })
//    @GetMapping("/product/{productId}")
//    public ResponseEntity<List<OrderDto>> getOrderListFromProduct(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @PathVariable Long productId
//    ){
//        Long userId = userDetails.getUser().getId();
//
//        return ResponseEntity.ok(
//                orderService.getOrderListFromProduct(userId, productId)
//        );
//
//    }

    @ApiOperation(value = "구매자의 구매내역 조회, 로그인필요")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = List.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @GetMapping("/user")
    public ResponseEntity<List<Map<LocalDate, List<OrderDto>>>> getPurchasedOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(orderService.getPurchasedOrderList(userId));
    }

    @ApiOperation(value = "판매자의 판매내역 조회, 로그인필요")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = List.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @GetMapping("/seller")
    public ResponseEntity<List<Map<LocalDate, List<OrderDto>>>> getSellerOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(orderService.getSellerOrderList(userId));
    }

//    @ApiOperation(value = "Deprecated: 개별주문 상세조회, 로그인필요")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Success", response = OrderDto.class),
//            @ApiResponse(code = 400, message = "Bad Request")
//    })
//    @GetMapping("/{orderId}")
//    public ResponseEntity<OrderDto> getOrder(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @PathVariable Long orderId
//    ){
//        Long userId = userDetails.getUser().getId();
//
//        return ResponseEntity.ok(
//                orderService.getOrder(orderId, userId)
//        );
//    }

//    /**
//     * 주문 수정
//     * @param
//     * @return
//     */
//    @ApiOperation(value = "Deprecated: 개별주문 수정, 로그인필요")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Success", response = String.class),
//            @ApiResponse(code = 400, message = "Bad Request")
//    })
//    @PutMapping
//    public ResponseEntity<String> modify(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @RequestBody PutOrderDto.PutOrderRequest putOrderRequest
//    ){
//        Long userId = userDetails.getUser().getId();
//        return ResponseEntity.ok(orderService.modifyOrder(putOrderRequest, userId));
//    }

    /**
     * 주문 개별삭제
     * @param orderId
     * @return
     */
    @ApiOperation(value = "개별주문 삭제, 로그인필요")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOne(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long orderId
    ){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(orderService.deleteOne(orderId, userId));
    }



}
