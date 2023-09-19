package com.github.commerce.service.order;

import com.github.commerce.entity.*;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.service.order.exception.OrderErrorCode;
import com.github.commerce.service.order.exception.OrderException;
import com.github.commerce.service.order.util.ValidateOrderMethod;
import com.github.commerce.web.dto.order.*;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ValidateOrderMethod validateOrderMethod;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public List<String> createOrder(List<PostOrderDto.PostOrderRequest> requestList, Long userId) {
        List<String >nameList = new ArrayList<>();
        for(PostOrderDto.PostOrderRequest request : requestList) {
            Long inputProductId = request.getProductId();
            Integer inputQuantity = request.getQuantity();
            List<String> inputOptions = request.getOptions();

            // Gson 인스턴스 생성
            Gson gson = new Gson();
            // inputOptions를 JSON 문자열로 변환
            String inputOptionsJson = gson.toJson(inputOptions);

            User validatedUser = validateOrderMethod.validateUser(userId);
            //TODO: 재고 소진 기능마련
            Product validatedProduct = validateOrderMethod.validateProduct(inputProductId);
            Seller validatedSeller = validatedProduct.getSeller();
            //TODO: 재고 부족 기능마련
            validateOrderMethod.validateStock(inputQuantity, validatedProduct);

            OrderRmqDto newOrder = OrderRmqDto.fromEntity(
                    Order.builder()
                            .users(validatedUser)
                            .sellers(validatedSeller)
                            .products(validatedProduct)
                            .createdAt(LocalDateTime.now())
                            .quantity(inputQuantity)
                            .orderState(1)
                            .totalPrice((long) (validatedProduct.getPrice() * inputQuantity))
                            .options(inputOptionsJson)
                            .build()
            );

            rabbitTemplate.convertAndSend("exchange", "postOrder", newOrder);
            nameList.add(validatedProduct.getName()+ "상품 주문요청");
        }
        return nameList;
    }

    @Transactional
    public List<String> createOrderFromCart(List<Long> cartIdList, Long userId) {
        User validatedUser = validateOrderMethod.validateUser(userId);
        List<String >nameList = new ArrayList<>();
        cartIdList.forEach(cartId -> {
            Cart validatedCart = validateOrderMethod.validateCart(cartId, userId);
            Product product = validatedCart.getProducts();
            Seller seller = product.getSeller();
            validateOrderMethod.validateStock(validatedCart.getQuantity(), product);

            OrderRmqDto newOrder = OrderRmqDto.fromEntity(
                    Order.builder()
                            .users(validatedUser)
                            .sellers(seller)
                            .products(product)
                            .createdAt(LocalDateTime.now())
                            .quantity(validatedCart.getQuantity())
                            .orderState(1)
                            .carts(validatedCart)
                            .totalPrice((long) product.getPrice() * validatedCart.getQuantity())
                            .options(validatedCart.getOptions())
                            .build()
            );

            rabbitTemplate.convertAndSend("exchange", "postOrder", newOrder);
            nameList.add(product.getName()+ "상품 주문요청");
        });

        return nameList;

    }


    @Transactional(readOnly = true)
    public List<Map<LocalDate, List<OrderDto>>> getPurchasedOrderList(Long userId){
        List<Order> sortedOrders = orderRepository.findPaidOrderByUserIdSortByCreatedAtDesc(userId);
        // 카트 레코드를 날짜별로 그룹화
        Map<LocalDate, List<OrderDto>> groupedOrders = sortedOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getCreatedAt().toLocalDate(),
                        Collectors.mapping(OrderDto::fromEntity, Collectors.toList())));

        List<Map<LocalDate, List<OrderDto>>> result = new ArrayList<>();
        result.add(groupedOrders);

        return result;
    }

    public List<Map<LocalDate, List<OrderDto>>> getSellerOrderList(Long userId) {
        Seller seller = validateOrderMethod.validateSellerByUserId(userId);

        List<Order> sortedOrders = orderRepository.findPaidOrderBySellerIdSortByCreatedAtDesc(seller.getId());
        Map<LocalDate, List<OrderDto>> groupedOrders = sortedOrders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getCreatedAt().toLocalDate(),
                        Collectors.mapping(OrderDto::fromEntity, Collectors.toList())));

        List<Map<LocalDate, List<OrderDto>>> result = new ArrayList<>();
        result.add(groupedOrders);

        return result;
    }


    @Transactional(readOnly = true)
    public List<OrderDto> getOrderList(Long userId){
        List<Order> sortedOrders = orderRepository.findAllByUsersIdOrderByCreatedAtDesc(userId);
        // 카트 레코드를 날짜별로 그룹화
//        Map<LocalDate, List<OrderDto>> groupedOrders = sortedOrders.stream()
//                .collect(Collectors.groupingBy(
//                        order -> order.getCreatedAt().toLocalDate(),
//                        Collectors.mapping(OrderDto::fromEntity, Collectors.toList())));
//
//        List<Map<LocalDate, List<OrderDto>>> result = new ArrayList<>();
//        result.add(groupedOrders);

        return sortedOrders.stream().map(OrderDto::fromEntity).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> getOrderListByCursor(Long userId, Long cursorId) {
        int pageSize = 10;
        Page<Order> orders = orderRepository.findAllByUsersIdAndCursorId(
                userId, cursorId, PageRequest.of(0, pageSize)
        ); //LIMIT 10의 우회적 구현

        return orders.map(OrderDto::fromEntity);
    }

//    @Transactional(readOnly = true)
//    public OrderDto getOrder(Long orderId, Long userId) {
//
//        return OrderDto.fromEntity(
//                orderRepository.findByIdAndUsersId(orderId, userId)
//                        .orElseThrow(() -> new OrderException(OrderErrorCode.THIS_ORDER_DOES_NOT_EXIST))
//        );
//    }

//    @Transactional
//    public String modifyOrder(PutOrderDto.PutOrderRequest request, Long userId) {
//        Long orderId = request.getOrderId();
//        Long productId = request.getProductId();
//        Integer inputQuantity = request.getQuantity();
//        List<String> options = request.getOptions();
//        // Gson 인스턴스 생성
//        Gson gson = new Gson();
//        // inputOptions를 JSON 문자열로 변환
//        String inputOptionsJson = gson.toJson(options);
//
//        User validatedUser = validateOrderMethod.validateUser(userId);
//        Order validatedOrder = validateOrderMethod.validateOrder(orderId, userId);
//        Product validatedProduct = validateOrderMethod.validateProduct(productId);
//        validateOrderMethod.validateStock(inputQuantity, validatedProduct);
//
//        OrderRmqDto newOrder = OrderRmqDto.fromEntityForModify(
//                Order.builder()
//                        .id(validatedOrder.getId())
//                        .users(validatedUser)
//                        .sellers(validatedProduct.getSeller())
//                        .products(validatedProduct)
//                        .createdAt(validatedOrder.getCreatedAt())
//                        .quantity(inputQuantity)
//                        .orderState(1)
//                        .totalPrice((long) (validatedProduct.getPrice() * inputQuantity))
//                        .options(inputOptionsJson)
//                        .build()
//        );
//        rabbitTemplate.convertAndSend("exchange", "putOrder", newOrder);
//        //return OrderDto.fromEntity(orderRepository.save(validatedOrder));
//        return validatedProduct.getName() + "상품 주문 수정";
//    }

    @Transactional
    public String deleteOne(Long orderId, Long userId) {
        validateOrderMethod.validateUser(userId);
        Order validatedOrder = validateOrderMethod.validateOrder(orderId, userId);
        orderRepository.deleteById(orderId);
        return validatedOrder.getId() + "번 주문 삭제";
    }



//    // 판매자에게 SSE 이벤트를 발생시키는 메서드
//    private void sendEventToSeller(Long sellerUserId, String message) {
//        String eventData = "data: {"
//                + "\"sellerUserId\": " + sellerUserId + ","
//                + "\"message\": \"" + message + "\""
//                + "}\n\n";
//
//
//        // SSE 이벤트를 발생시키는 WebClient를 사용하여 /sse/sendEvent 엔드포인트로 요청을 보냅니다.
//        webClientBuilder.baseUrl("http://localhost:8080/sse") // SSEController의 엔드포인트 URL로 설정
//                .build()
//                .get()
//                .uri("/sendEvent")
//                .header("Content-Type", "text/event-stream") // SSE 이벤트 타입으로 설정
//                .bodyValue(eventData) // 생성한 이벤트 데이터를 body에 넣습니다.
//                .exchange()
//                .flatMap(response -> {
//                    if (response.statusCode().is2xxSuccessful()) {
//                        return Mono.empty(); // 성공적으로 요청을 보내면 빈 Mono를 반환합니다.
//                    } else {
//                        return Mono.error(new RuntimeException("SSE 이벤트 발생에 실패했습니다."));
//                    }
//                })
//                .block(); // 블로킹 방식으로 요청을 보냅니다.
//    }

}
