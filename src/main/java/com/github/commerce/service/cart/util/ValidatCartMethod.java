package com.github.commerce.service.cart.util;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.cart.exception.CartErrorCode;
import com.github.commerce.service.cart.exception.CartException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Transactional
public class ValidatCartMethod {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;


    public User validateUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new CartException(CartErrorCode.USER_NOT_FOUND));
    }

    public void validateStock(Integer inputQuantity, Product product){
        if (inputQuantity <= 0 || inputQuantity > product.getLeftAmount()) {
            throw new CartException(CartErrorCode.INVALID_QUANTITY);
        }
    }

    public Product validateProduct(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CartException(CartErrorCode.THIS_PRODUCT_DOES_NOT_EXIST));

        Integer stock = product.getLeftAmount();
        if (stock == null || stock <= 0) {
            throw new CartException(CartErrorCode.OUT_OF_STOCK);
        }

        return product;
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

    public void validateDuplicateCart(Product validatedProduct, Long userId, String options) {
        Long productId = validatedProduct.getId();
        List<Cart> cartList = cartRepository.findAllByProductsIdAndUsersId(productId, userId);
        cartList.forEach((cart)->{
            if(Objects.equals(validatedProduct.getId(), cart.getProducts().getId()) && Objects.equals(cart.getOptions(), options)){
                throw new CartException(CartErrorCode.PRODUCT_DUPLICATE, productId, options);
            }
        });
    }
}
