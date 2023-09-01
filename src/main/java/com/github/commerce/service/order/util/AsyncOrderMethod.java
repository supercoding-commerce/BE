package com.github.commerce.service.order.util;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Order;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import com.github.commerce.entity.mongocollection.CartSavedOption;
import com.github.commerce.entity.mongocollection.OrderSavedOption;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.cart.CartSavedOptionRepository;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.repository.order.OrderSavedOptionRepository;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.cart.exception.CartErrorCode;
import com.github.commerce.service.cart.exception.CartException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Transactional
public class AsyncOrderMethod {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartSavedOptionRepository cartSavedOptionRepository;
    private final OrderSavedOptionRepository orderSavedOptionRepository;
    private final ValidateOrderMethod validateOrderMethod;
    @Async
    public CompletableFuture<OrderSavedOption> updateOrderMongoDB(Long orderId, Long userId, Map<String, String> options) {

        Order validatedOrder = validateOrderMethod.validateOrder(orderId, userId);
        OrderSavedOption savedOption = orderSavedOptionRepository.findByOrderId(validatedOrder.getId());
        // 조회한 데이터 업데이트
        if (savedOption != null) {
            savedOption.setOptions(options);
            orderSavedOptionRepository.save(savedOption); // 업데이트된 값을 저장
            return CompletableFuture.completedFuture(savedOption);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Order> updateOrderMySQL(Long orderId, Long userId, Long productId, Integer quantity) {
        Order validatedOrder = validateOrderMethod.validateOrder(orderId, userId);
        Product validatedProduct = validateOrderMethod.validateProduct(productId);
        validateOrderMethod.validateStock(quantity, validatedProduct);

        validatedOrder.setQuantity(quantity);
        orderRepository.save(validatedOrder);
        return CompletableFuture.completedFuture(validatedOrder);

    }

    @Async
    public void deleteOptionByOrderId(Long orderId) {
        orderSavedOptionRepository.deleteByOrderId(orderId);
    }

}
