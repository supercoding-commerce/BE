package com.github.commerce.service.cart.util;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import com.github.commerce.entity.mongocollection.CartSavedOption;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.cart.CartSavedOptionRepository;
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
public class AsyncCartMethod {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartSavedOptionRepository cartSavedOptionRepository;
    private final ValidatCartMethod validatCartMethod;

    /**
     * MongoDB 옵션 관련 메서드 주석처리
     * @param cartId
     * @param userId
     * @param productId
     * @param quantity
     * @return
     */
//    @Async
//    public CompletableFuture<CartSavedOption> updateCartMongoDB(Long cartId, Long userId, Map<String, String> options) {
//
//        Cart validatedCart = validateCart(cartId, userId);
//        CartSavedOption savedOption = cartSavedOptionRepository.findByOptionId(validatedCart.getOptionId());
//        // 조회한 데이터 업데이트
//        if (savedOption != null) {
//            savedOption.setOptions(options);
//            cartSavedOptionRepository.save(savedOption); // 업데이트된 값을 저장
//            return CompletableFuture.completedFuture(savedOption);
//        }
//        return CompletableFuture.completedFuture(null);
//    }

    @Async
    public CompletableFuture<Cart> updateCartMySQL(Cart validatedCart) {

        Cart savedCart = cartRepository.save(validatedCart);
        return CompletableFuture.completedFuture(savedCart);

    }

    @Async
    public void deleteOptionByOptionId(String optionId) {
        cartSavedOptionRepository.deleteByOptionId(optionId);
    }

    @Async
    public void deleteAllByUsersId(Long userId) {
        cartSavedOptionRepository.deleteAllByUserId(userId);
    }

}
