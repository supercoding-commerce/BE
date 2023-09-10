package com.github.commerce.service.product.util;

import com.github.commerce.entity.Seller;
import com.github.commerce.entity.User;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.user.SellerRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.order.exception.OrderErrorCode;
import com.github.commerce.service.order.exception.OrderException;
import com.github.commerce.service.product.exception.ProductErrorCode;
import com.github.commerce.service.product.exception.ProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional
public class ValidateProductMethod {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final SellerRepository sellerRepository;

    public User validateUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.USER_NOT_FOUND));
    }

    public Seller validateSeller(Long userId){
        return sellerRepository.findByUsersId(userId)
                .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_REGISTERED_SELLER));
    }

    public boolean isThisProductSeller(Long sellerId, Long userId) {
        Optional<Seller> sellerOptional = sellerRepository.findByUsersId(userId);
        return sellerOptional.isPresent() && sellerOptional.get().getId().equals(sellerId);
    }
}
