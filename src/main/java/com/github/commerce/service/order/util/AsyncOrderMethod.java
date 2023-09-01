package com.github.commerce.service.order.util;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import com.github.commerce.entity.mongocollection.CartSavedOption;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.cart.CartSavedOptionRepository;
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
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartSavedOptionRepository cartSavedOptionRepository;
    private final OrderSavedOptionRepository orderSavedOptionRepository;
    @Async
    public CompletableFuture<CartSavedOption> updateCartMongoDB(Long cartId, Long userId, Map<String, String> options) {

        Cart validatedCart = validateCart(cartId, userId);
        CartSavedOption savedOption = cartSavedOptionRepository.findByOptionId(validatedCart.getOptionId());
        // 조회한 데이터 업데이트
        if (savedOption != null) {
            savedOption.setOptions(options);
            cartSavedOptionRepository.save(savedOption); // 업데이트된 값을 저장
            return CompletableFuture.completedFuture(savedOption);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Cart> updateCartMySQL(Long cartId, Long userId, Long productId, Integer quantity) {
        Cart validatedCart = validateCart(cartId, userId);
        Product validatedProduct = validateProduct(productId);
        validateStock(quantity, validatedProduct);

        validatedCart.setQuantity(quantity);
        cartRepository.save(validatedCart);
        return CompletableFuture.completedFuture(validatedCart);

    }

    @Async
    public void deleteOptionByOrderId(Long orderId) {
        orderSavedOptionRepository.deleteByOrderId(orderId);
    }

    @Async
    public void deleteAllByUsersId(Long userId) {
        cartSavedOptionRepository.deleteAllByUserId(userId);
    }

    private User validateUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new CartException(CartErrorCode.USER_NOT_FOUND));
    }

    private void validateStock(Integer inputQuantity, Product product){
        if (inputQuantity <= 0 || inputQuantity > product.getLeftAmount()) {
            throw new CartException(CartErrorCode.INVALID_QUANTITY);
        }
    }

    private Product validateProduct(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CartException(CartErrorCode.THIS_PRODUCT_DOES_NOT_EXIST));

        Long stock = product.getLeftAmount();
        if (stock == null || stock <= 0) {
            throw new CartException(CartErrorCode.OUT_OF_STOCK);
        }

        return product;
    }

    private Cart validateCart(Long cartId, Long userId){
        Cart cart = cartRepository.findByIdAndUsersId(cartId, userId);

        if (cart == null) {
            throw new CartException(CartErrorCode.THIS_CART_DOES_NOT_EXIST);
        }
        return cart;
    }

    private boolean existsInCart(Long userId, Long productId){
        return cartRepository.existsByUsersIdAndProductsId(userId, productId);
    }



}
