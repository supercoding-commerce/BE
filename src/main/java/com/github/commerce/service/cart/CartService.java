package com.github.commerce.service.cart;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.cart.util.AsyncCartMethod;
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

    @Transactional(readOnly = true)
    public List<List<CartDto>> getAllCarts(Long userId){
        List<Cart> sortedCarts = cartRepository.findAllByUsersIdOrderByCreatedAtDesc(userId);
        // 카트 레코드를 날짜별로 그룹화
        Map<LocalDate, List<Cart>> groupedCarts = sortedCarts.stream()
                .collect(Collectors.groupingBy(cart -> cart.getCreatedAt().toLocalDate()));


        // 각 그룹을 CartDto 리스트로 변환
        List<List<CartDto>> result = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Cart>> entry : groupedCarts.entrySet()) {
            List<CartDto> cartDtos = entry.getValue().stream()
                    .map(CartDto::fromEntity)
                    .collect(Collectors.toList());
            result.add(cartDtos);
        }

        return result;
    }

    @Transactional(readOnly = true)
    public Page<CartDto> getAllCartWithCursor(Long userId, Long cursorId) {
        int pageSize = 10;
        Page<Cart> carts = cartRepository.findAllByUserId(
               userId, cursorId, PageRequest.of(0, pageSize)
        ); //LIMIT 10의 우회적 구현

        //List<CartSavedOption> optionList = cartSavedOptionRepository.findCartSavedOptionsByUserId(userId);
//        return carts.map(cart -> {
//            CartSavedOption matchedOption = optionList.stream()
//                    .filter(option -> option.getOptionId().equals(cart.getOptionId()))
//                    .findFirst().orElseGet(CartSavedOption::new); // 빈 값으로 초기화
//
//            return CartDto.fromEntity(cart, matchedOption.getOptions());
//        });
        return carts.map(CartDto::fromEntity);
    }


    //thenCompose: 이 메서드는 첫 번째 CompletableFuture의 결과를 사용하여 두 번째 CompletableFuture를 실행하는데 사용됩니다.
    // 즉, 첫 번째 작업이 끝난 후 그 결과를 다음 작업에 전달하여 순차적으로 실행됩니다.
    // 이를 통해 비동기 작업을 순차적으로 연결하거나, 한 작업의 결과를 다른 작업에 전달하여 활용할 수 있습니다.
    @Transactional
    public String addToCart(PostCartDto.Request request, Long userId) {
        Long inputProductId = request.getProductId();
        Integer inputQuantity = request.getQuantity();
        List<Map<String, String>> inputOptions = request.getOptions();

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
    public String modifyCart(PutCartDto.Request request, Long userId) {
        Long cartId = request.getCartId();
        Long productId = request.getProductId();
        Integer inputQuantity = request.getQuantity();
        List<Map<String, String>> options = request.getOptions();

        // Gson 인스턴스 생성
        Gson gson = new Gson();

        // inputOptions를 JSON 문자열로 변환
        String inputOptionsJson = gson.toJson(options);

        User validatedUser = validatCartMethod.validateUser(userId);
        Cart validatedCart = validatCartMethod.validateCart(cartId, userId);
        Product validatedProduct = validatCartMethod.validateProduct(productId);
        validatCartMethod.validateStock(inputQuantity, validatedProduct);

//        validatedCart.setQuantity(quantity);
//        validatedCart.setOptions(inputOptionsJson);
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
        return validatedProduct.getName() + "상품을 장바구니서 수정합니다.";

//        return CartDto.fromEntity(
//                cartRepository.save(validatedCart)
//        );
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
