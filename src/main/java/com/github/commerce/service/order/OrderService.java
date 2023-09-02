package com.github.commerce.service.order;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Order;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import com.github.commerce.entity.mongocollection.CartSavedOption;
import com.github.commerce.entity.mongocollection.OrderSavedOption;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.repository.order.OrderSavedOptionRepository;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.cart.exception.CartErrorCode;
import com.github.commerce.service.cart.exception.CartException;
import com.github.commerce.service.order.exception.OrderErrorCode;
import com.github.commerce.service.order.exception.OrderException;
import com.github.commerce.service.order.util.AsyncOrderMethod;
import com.github.commerce.service.order.util.ValidateOrderMethod;
import com.github.commerce.web.controller.order.SSEController;
import com.github.commerce.web.dto.cart.CartDto;
import com.github.commerce.web.dto.order.DelayedOrderDto;
import com.github.commerce.web.dto.order.OrderDto;
import com.github.commerce.web.dto.order.PostOrderDto;
import com.github.commerce.web.dto.order.PutOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderSavedOptionRepository orderSavedOptionRepository;
    private final CartRepository cartRepository;
    private final AsyncOrderMethod asyncOrderMethod;
    private final ValidateOrderMethod validateOrderMethod;
    private WebClient.Builder webClientBuilder;
    private SSEController sseController;

    @Transactional
    public OrderDto createOrder(PostOrderDto.Request request, Long userId) {
        Long inputProductId = request.getProductId();
        Integer inputQuantity = request.getQuantity();
        Map<String, String> inputOptions = request.getOptions();
        Long inputCartId = request.getCartId();
        Cart validatedCart = null;

        if (inputCartId != null) {
            validatedCart = validateOrderMethod.validateCart(inputCartId, userId);
        }

        User validatedUser = validateOrderMethod.validateUser(userId);
        Product validatedProduct = validateOrderMethod.validateProduct(inputProductId);

        try {
            validateOrderMethod.validateStock(inputQuantity, validatedProduct);
        }catch(OrderException e){
            // 상품 재고 부족 에러가 발생한 경우
            // 에러 메시지를 그대로 던지지 않고 메시지 큐에 정보를 전달
            DelayedOrderDto delayedOrder = DelayedOrderDto.builder()
                    .userId(userId)
                    .productId(inputProductId)
                    .quantity(inputQuantity)
                    .options(inputOptions)
                    .build();
            //messageQueueService.enqueueOrderRequest(delayedOrder);

            //판매자에게 알람
            //sendEventToSeller(validatedProduct.getUsers().getId(), "상품 재고 부족 알림");

            throw e;
        }

        Order savedOrder = orderRepository.save(
                Order.builder()
                .users(validatedUser)
                .products(validatedProduct)
                .createdAt(LocalDateTime.now())
                .quantity(inputQuantity)
                .orderState(1)
                .carts(validatedCart)
                .total_price((int) (validatedProduct.getPrice() * inputQuantity))
                .build()
        );

        Long orderId = savedOrder.getId();
        OrderSavedOption orderSavedOption = OrderSavedOption.builder()
                .userId(userId)
                .productId(inputProductId)
                .orderId(orderId)
                .options(inputOptions)
                .build();
        orderSavedOptionRepository.save(orderSavedOption);

        if(validatedCart != null){
            validatedCart.setIsOrdered(true);
            cartRepository.save(validatedCart);
        }


        return OrderDto.fromEntity(orderRepository.save(savedOrder), inputOptions);
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> getOrderList(Long userId, Long cursorId) {
        int pageSize = 10;
        Page<Order> orders = orderRepository.findAllByUsersIdAndCursorId(
                userId, cursorId, PageRequest.of(0, pageSize)
        ); //LIMIT 10의 우회적 구현

        List<OrderSavedOption> optionList = orderSavedOptionRepository.findAllByUserId(userId);
        return orders.map(order -> {
            OrderSavedOption matchedOption = optionList.stream()
                    .filter(option -> option.getOrderId().equals(order.getId()))
                    .findFirst().orElseGet(OrderSavedOption::new); // 빈 값으로 초기화

            return OrderDto.fromEntity(order, matchedOption.getOptions());
        });
    }

    @Transactional(readOnly = true)
    public OrderDto getOrder(Long orderId, Long userId) {

        OrderSavedOption savedOption = orderSavedOptionRepository.findByOrderIdAndUserId(orderId, userId);
        if(savedOption == null){
            savedOption = new OrderSavedOption();
        }
        return OrderDto.fromEntity(
                orderRepository.findByIdAndUsersId(orderId, userId)
                        .orElseThrow(() -> new OrderException(OrderErrorCode.THIS_ORDER_DOES_NOT_EXIST)),
                savedOption.getOptions()
        );
    }

    @Transactional
    public OrderDto modifyOrder(PutOrderDto.Request request, Long userId) {
        Long orderId = request.getOrderId();
        Long productId = request.getProductId();
        Integer quantity = request.getQuantity();
        Map<String, String> options = request.getOptions();

        validateOrderMethod.validateUser(userId);

        CompletableFuture<OrderSavedOption> savedOptionFuture = asyncOrderMethod.updateOrderMongoDB(orderId, userId, options);
        CompletableFuture<Order> savedOrderFuture = asyncOrderMethod.updateOrderMySQL(orderId, userId, productId, quantity);

        CompletableFuture<OrderDto> combinedFuture = savedOptionFuture.thenCombine(
                savedOrderFuture,
                (savedOption, savedOrder) -> OrderDto.fromEntity(savedOrder, savedOption.getOptions())
        ).exceptionally(exception -> {
            // 예외 처리 로직 추가
            exception.printStackTrace();
            return null; // 또는 예외 상황에 대한 대체 값을 반환
        });
        return combinedFuture.join();
    }

    @Transactional
    public String deleteOne(Long orderId, Long userId) {
        validateOrderMethod.validateUser(userId);
        Order validatedOrder = validateOrderMethod.validateOrder(orderId, userId);
        asyncOrderMethod.deleteOptionByOrderId(validatedOrder.getId());
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
