package com.github.commerce.service.order;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Order;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.User;
import com.github.commerce.entity.mongocollection.OrderSavedOption;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.repository.order.OrderSavedOptionRepository;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.user.UserRepository;
import com.github.commerce.service.cart.exception.CartErrorCode;
import com.github.commerce.service.cart.exception.CartException;
import com.github.commerce.service.order.exception.OrderErrorCode;
import com.github.commerce.service.order.exception.OrderException;
import com.github.commerce.service.order.util.AsyncOrderMethod;
import com.github.commerce.web.dto.order.OrderDto;
import com.github.commerce.web.dto.order.PostOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderSavedOptionRepository orderSavedOptionRepository;
    private final CartRepository cartRepository;
    private final AsyncOrderMethod asyncOrderMethod;

    @Transactional
    public OrderDto createOrder(PostOrderDto.Request request, Long userId) {
        Long inputProductId = request.getProductId();
        Integer inputQuantity = request.getQuantity();
        Map<String, String> inputOptions = request.getOptions();
        Long inputCartId = request.getCartId();
        Cart validatedCart = null;

        if (inputCartId != null) {
            validatedCart = validateCart(inputCartId);
        }

        User validatedUser = validateUser(userId);
        Product validatedProduct = validateProduct(inputProductId);
        validateStock(inputQuantity, validatedProduct);

        Order savedOrder = orderRepository.save(
                Order.builder()
                .users(validatedUser)
                .products(validatedProduct)
                .createdAt(LocalDateTime.now())
                .quantity(inputQuantity)
                .orderState(1)
                .carts(validatedCart)
                .total_price((int) (validatedProduct.getPrice() * inputQuantity))
                .build()
        );

        Long orderId = savedOrder.getId();
        OrderSavedOption orderSavedOption = OrderSavedOption.builder()
                .userId(userId)
                .productId(inputProductId)
                .orderId(orderId)
                .options(inputOptions)
                .build();
        orderSavedOptionRepository.save(orderSavedOption);

        if(validatedCart != null){
            validatedCart.setIsOrdered(true);
            cartRepository.save(validatedCart);
        }


        return OrderDto.fromEntity(orderRepository.save(savedOrder), inputOptions);
    }

    @Transactional(readOnly = true)
    public Page<OrderDto> getOrderList(Long userId, Long cursorId) {
        int pageSize = 10;
        Page<Order> orders = orderRepository.findAllByUsersIdAndCursorId(
                userId, cursorId, PageRequest.of(0, pageSize)
        ); //LIMIT 10의 우회적 구현

        List<OrderSavedOption> optionList = orderSavedOptionRepository.findAllByUserId(userId);
        return orders.map(order -> {
            OrderSavedOption matchedOption = optionList.stream()
                    .filter(option -> option.getOrderId().equals(order.getId()))
                    .findFirst().orElseGet(OrderSavedOption::new); // 빈 값으로 초기화

            return OrderDto.fromEntity(order, matchedOption.getOptions());
        });
    }

    @Transactional
    public String deleteOne(Long orderId, Long userId) {
        validateUser(userId);
        Order validatedOrder = validateOrder(orderId, userId);
        asyncOrderMethod.deleteOptionByOrderId(validatedOrder.getId());
        orderRepository.deleteById(orderId);
        return validatedOrder.getId() + "번 주문 삭제";
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

    private Cart validateCart(Long cartId){
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new CartException(CartErrorCode.THIS_CART_DOES_NOT_EXIST));
    }


    private Order validateOrder(Long orderId, Long userId) {
        return orderRepository.findByIdAndUsersId(orderId, userId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.THIS_ORDER_DOES_NOT_EXIST));
    }
}
