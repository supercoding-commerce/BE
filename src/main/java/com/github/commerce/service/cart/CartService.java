package com.github.commerce.service.cart;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.mongocollection.CartSavedOption;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.cart.CartSavedOptionRepository;
import com.github.commerce.web.dto.cart.CartDto;
import com.github.commerce.web.dto.cart.PostCartDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartSavedOptionRepository cartSavedOptionRepository;
    @Transactional(readOnly = true)
    public Page<CartDto> getAllCart(Long userId, Long cursorId) {
        int pageSize = 10;
        Page<Cart> carts = cartRepository.findAllByCartId(
                cursorId, PageRequest.of(0, pageSize)
        ); //LIMIT 10의 우회적 구현

        List<CartSavedOption> optionList = cartSavedOptionRepository.findCartSavedOptionsByUserId(userId);
        return carts.map(cart -> {
            CartSavedOption matchedOption = optionList.stream()
                    .filter(option -> option.getOptionId().equals(cart.getOptionId()))
                    .findFirst().orElseGet(CartSavedOption::new); // 빈 값으로 초기화

            return CartDto.fromEntity(cart, matchedOption.getOptions());
        });
    }

    @Transactional
    public CartDto addToCart(PostCartDto.Request request, Long userId) {
        Long inputProductId = request.getProductId();
        Integer inputQuantity = request.getQuantity();
        Map<String, String> inputOptions = request.getOptions();

        CartSavedOption savedOption = cartSavedOptionRepository.save(
                CartSavedOption.builder()
                        .userId(userId)
                        .productId(inputProductId)
                        .options(inputOptions)
                        .build()
        );

        String optionId = savedOption.getOptionId();

        return CartDto.fromEntity(
                cartRepository.save(
                        Cart.builder()
                                .optionId(optionId)
                                .createdAt(LocalDateTime.now())
                                .quantity(inputQuantity)
                                .orderState(1) //기본값 주문대기
                                .build()
                        ),
                inputOptions
                );
    }
}
