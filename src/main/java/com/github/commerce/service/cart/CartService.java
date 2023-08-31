package com.github.commerce.service.cart;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.mongocollection.CartSavedOption;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.cart.CartSavedOptionRepository;
import com.github.commerce.service.cart.util.AsyncMethod;
import com.github.commerce.web.dto.cart.CartDto;
import com.github.commerce.web.dto.cart.PostCartDto;
import com.github.commerce.web.dto.cart.PutCartDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartSavedOptionRepository cartSavedOptionRepository;
    private final AsyncMethod asyncMethod;

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


    //thenCompose: 이 메서드는 첫 번째 CompletableFuture의 결과를 사용하여 두 번째 CompletableFuture를 실행하는데 사용됩니다.
    // 즉, 첫 번째 작업이 끝난 후 그 결과를 다음 작업에 전달하여 순차적으로 실행됩니다.
    // 이를 통해 비동기 작업을 순차적으로 연결하거나, 한 작업의 결과를 다른 작업에 전달하여 활용할 수 있습니다.
    @Transactional
    public CartDto addToCart(PostCartDto.Request request, Long userId) {
        Long inputProductId = request.getProductId();
        Integer inputQuantity = request.getQuantity();
        Map<String, String> inputOptions = request.getOptions();

        CompletableFuture<CartSavedOption> savedOptionFuture = CompletableFuture.supplyAsync(() ->
                cartSavedOptionRepository.save(
                        CartSavedOption.builder()
                                .userId(userId)
                                .productId(inputProductId)
                                .options(inputOptions)
                                .build()
                )
        );

        return savedOptionFuture.thenCompose(savedOption -> {
            String optionId = savedOption.getOptionId();
            return CompletableFuture.supplyAsync(() ->
                    cartRepository.save(
                            Cart.builder()
                                    .optionId(optionId)
                                    .createdAt(LocalDateTime.now())
                                    .quantity(inputQuantity)
                                    .orderState(1) //기본값 주문대기
                                    .build()
                    )
            );
        }).thenApply(savedCart -> CartDto.fromEntity(savedCart, inputOptions)).join();
    }

    //thenCombine
    // 두 개의 CompletableFuture의 결과를 조합하여 새로운 결과를 생성
    // 두 작업이 병렬적으로 실행되며, 두 작업의 결과가 모두 준비되면 조합 작업이 실행
    @Transactional
    public CartDto modifyCart(PutCartDto.Request request, Long userId) {
        Long cartId = request.getCartId();
        Long productId = request.getProductId();
        Integer quantity = request.getQuantity();
        String optionId = request.getOptionId();
        Map<String, String> options = request.getOptions();

        CompletableFuture<CartSavedOption> savedOptionFuture = asyncMethod.updateCartMongoDB(optionId, options);
        CompletableFuture<Cart> savedCartFuture = asyncMethod.updateCartMySQL(cartId, quantity);

        CompletableFuture<CartDto> combinedFuture = savedOptionFuture.thenCombine(
                savedCartFuture,
                (savedOption, savedCart) -> CartDto.fromEntity(savedCart, savedOption.getOptions())
        ).exceptionally(exception -> {
            // 예외 처리 로직 추가
            exception.printStackTrace();
            return null; // 또는 예외 상황에 대한 대체 값을 반환
        });
        return combinedFuture.join();
        //join() 메서드는 호출하는 쓰레드를 해당 CompletableFuture가 완료될 때까지 블록하고 결과를 반환합니다.
        // get() 메서드와 비슷하지만, 확인된 예외 없이 예외를 처리합니다.
        // 만약 내부 비동기 작업에서 예외가 발생하면, join()을 호출할 때 확인되지 않은 예외로 던져집니다.
    }


    public void deleteAll(Long userId) {

    }
}
