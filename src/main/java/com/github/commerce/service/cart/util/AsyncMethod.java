package com.github.commerce.service.cart.util;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.mongocollection.CartSavedOption;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.cart.CartSavedOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Transactional
public class AsyncMethod {
    private final CartRepository cartRepository;
    private final CartSavedOptionRepository cartSavedOptionRepository;
    @Async
    public CompletableFuture<CartSavedOption> updateCartMongoDB(String optionId, Map<String, String> options) {

        CartSavedOption savedOption = cartSavedOptionRepository.findByOptionId(optionId);
        // 조회한 데이터 업데이트
        if (savedOption != null) {
            savedOption.setOptions(options);
            cartSavedOptionRepository.save(savedOption); // 업데이트된 값을 저장
            return CompletableFuture.completedFuture(savedOption);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Cart> updateCartMySQL(Long cartId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                //todo: 예외처리
        );

        cart.setQuantity(quantity);
        cartRepository.save(cart);
        return CompletableFuture.completedFuture(cart);

    }
}
