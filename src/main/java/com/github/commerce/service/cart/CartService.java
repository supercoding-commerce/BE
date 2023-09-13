package com.github.commerce.service.cart;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.service.cart.util.ValidatCartMethod;
import com.github.commerce.web.dto.cart.CartDto;
import com.github.commerce.web.dto.cart.CartRmqDto;
import com.github.commerce.web.dto.cart.PostCartDto;
import com.github.commerce.web.dto.cart.PutCartDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ValidatCartMethod validatCartMethod;
    private final RabbitTemplate rabbitTemplate;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Map<LocalDate, List<CartDto>>> getAllCarts(Long userId){
        List<Cart> sortedCarts = cartRepository.findAllByUsersIdOrderByCreatedAtDesc(userId);
        // 카트 레코드를 날짜별로 그룹화
        //여기서 중요한 점은 LocalDate는 날짜만을 다루기 때문에 시간 정보가 무시되고 날짜 정보만을 사용하여 그룹화가 이루어집니다.
        // 만약 시간 정보도 포함하여 그룹화하려면 LocalDateTime을 사용하거나 다른 방식으로 날짜와 시간을 함께 처리해야 합니다.
        Map<LocalDate, List<CartDto>> groupedCarts = sortedCarts.stream()
                .collect(Collectors.groupingBy(
                        cart -> cart.getCreatedAt().toLocalDate(),
                        Collectors.mapping(CartDto::fromEntity, Collectors.toList())
                ));

        List<Map<LocalDate, List<CartDto>>> result = new ArrayList<>();
        result.add(groupedCarts);

        return result;

    }

    @Transactional(readOnly = true)
    public Page<CartDto> getAllCartWithCursor(Long userId, Long cursorId) {
        int pageSize = 10;
        Page<Cart> carts = cartRepository.findAllByUserId(
               userId, cursorId, PageRequest.of(0, pageSize)
        ); //LIMIT 10의 우회적 구현

        return carts.map(CartDto::fromEntity);
    }

    @Transactional
    public String addToCart(PostCartDto.PostCartRequest request, Long userId) {
        Long inputProductId = request.getProductId();
        Integer inputQuantity = request.getQuantity();
        List<String> inputOptions = request.getOptions();

        // Gson 인스턴스 생성
        Gson gson = new Gson();

        // inputOptions를 JSON 문자열로 변환
        String inputOptionsJson = gson.toJson(inputOptions);

        User validatedUser = validatCartMethod.validateUser(userId);
        Product validatedProduct = validatCartMethod.validateProduct(inputProductId);
        validatCartMethod.validateStock(inputQuantity, validatedProduct);

        CartRmqDto newCart = CartRmqDto.fromEntityForPost(
                Cart.builder()
                        .users(validatedUser)
                        .products(validatedProduct)
                        .options(inputOptionsJson)
                        .quantity(inputQuantity)
                        .isOrdered(false)
                        .build()
        );

        rabbitTemplate.convertAndSend("exchange", "postCart", newCart);
        return validatedProduct.getName() + "상품을 장바구니에 넣습니다.";
    }

    @Transactional
    public List<String> modifyCart(List<PutCartDto.PutCartRequest> requestList, Long userId) {
        List<String >nameList = new ArrayList<>();
        for(PutCartDto.PutCartRequest request : requestList) {
            Long cartId = request.getCartId();
            Long productId = request.getProductId();
            Integer inputQuantity = request.getQuantity();
            List<String> options = request.getOptions();

            // Gson 인스턴스 생성
            Gson gson = new Gson();

            // inputOptions를 JSON 문자열로 변환
            String inputOptionsJson = gson.toJson(options);

            User validatedUser = validatCartMethod.validateUser(userId);
            Cart validatedCart = validatCartMethod.validateCart(cartId, userId);
            Product validatedProduct = validatCartMethod.validateProduct(productId);
            validatCartMethod.validateStock(inputQuantity, validatedProduct);

            CartRmqDto newCart = CartRmqDto.fromEntityForModify(
                    Cart.builder()
                            .id(validatedCart.getId())
                            .users(validatedUser)
                            .products(validatedProduct)
                            .options(inputOptionsJson)
                            .quantity(inputQuantity)
                            .isOrdered(false)
                            .createdAt(validatedCart.getCreatedAt())
                            .build()
            );
            rabbitTemplate.convertAndSend("exchange", "putCart", newCart);
            nameList.add(validatedProduct.getName() + "상품을 장바구니서 수정합니다.");

        }
        return nameList;

    }


    @Transactional
    public String deleteAll(Long userId) {
        User validatedUser = validatCartMethod.validateUser(userId);
        //asyncCartMethod.deleteAllByUsersId(userId);
        cartRepository.deleteAllByUsersId(userId);
        return validatedUser.getUserName() + "님의 장바구니 목록이 삭제되었습니다.";
    }

    @Transactional
    public String deleteOne(Long cartId, Long userId){
        validatCartMethod.validateUser(userId);
        Cart validatedCart = validatCartMethod.validateCart(cartId, userId);
        //asyncCartMethod.deleteOptionByOptionId(validatedCart.getOptionId());
        cartRepository.deleteById(cartId);
        return validatedCart.getId() + "번 장바구니 삭제";
    }


}
