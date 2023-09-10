package com.github.commerce.service.order.util;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Order;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.user.SellerRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.cart.exception.CartErrorCode;
import com.github.commerce.service.cart.exception.CartException;
import com.github.commerce.service.order.exception.OrderErrorCode;
import com.github.commerce.service.order.exception.OrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ValidateOrderMethod {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final SellerRepository sellerRepository;

    public User validateUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.USER_NOT_FOUND));
    }

    public Order validateOrder(Long orderId, Long userId) {
        return orderRepository.findByIdAndUsersId(orderId, userId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.THIS_ORDER_DOES_NOT_EXIST));
    }

    public Product validateProduct(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.THIS_PRODUCT_DOES_NOT_EXIST));

        Integer stock = product.getLeftAmount();
        if (stock == null || stock <= 0) {
            throw new OrderException(OrderErrorCode.OUT_OF_STOCK);
        }

        return product;
    }

    public void validateStock(Integer inputQuantity, Product product){
        if (inputQuantity <= 0 || inputQuantity > product.getLeftAmount()) {
            throw new OrderException(OrderErrorCode.INVALID_QUANTITY);
        }
    }

    public Cart validateCart(Long cartId, Long userId){
        Cart cart = cartRepository.findByIdAndUsersId(cartId, userId);

        if (cart == null) {
            throw new CartException(CartErrorCode.THIS_CART_DOES_NOT_EXIST);
        }
        return cart;
    }


    private boolean existsInCart(Long userId, Long productId){
        return cartRepository.existsByUsersIdAndProductsId(userId, productId);
    }

    public boolean validateSellerByUserId(Long userId) {
        return sellerRepository.existsByUsersId(userId);
    }
}
