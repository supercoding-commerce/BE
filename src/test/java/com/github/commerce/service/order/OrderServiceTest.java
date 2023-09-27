package com.github.commerce.service.order;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.Seller;
import com.github.commerce.entity.User;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.service.order.util.OrderCacheMethod;
import com.github.commerce.service.order.util.ValidateOrderMethod;
import com.github.commerce.web.dto.order.OrderRmqDto;
import com.github.commerce.web.dto.order.PostOrderDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@TestPropertySource(locations = "classpath:application-test.yml")
@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정
class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderCacheMethod orderCacheMethod;
    @Mock
    private ValidateOrderMethod validateOrderMethod;
    @Mock
    private RabbitTemplate rabbitTemplate;

    static Stream<Arguments> provideOrderTestData() {
        return Stream.of(
                Arguments.of(1L, 2, Arrays.asList("Option1")),
                Arguments.of(2L, 3, Arrays.asList("Option2")),
                Arguments.of(3L, 4, Arrays.asList("Option3"))
        );
    }

    static Stream<Arguments> provideOrderFromCartTestData() {
        return Stream.of(
                Arguments.of(Arrays.asList(1L, 2L, 3L))
        );
    }

    @ParameterizedTest
    @MethodSource("provideOrderTestData")
    @DisplayName("주문 생성")
    void createOrder(Long productId, Integer quantity, List<String> options) {
        PostOrderDto.PostOrderRequest request = new PostOrderDto.PostOrderRequest(productId, quantity, options);
        List<PostOrderDto.PostOrderRequest> requestList = new ArrayList<>();
        requestList.add(request);

        Long userId = 1L;
        User user = new User();
        Product product = new Product();
        product.setName("Product" + productId);
        product.setPrice(100);
        Seller seller = new Seller();
        product.setSeller(seller);


        when(validateOrderMethod.validateUser(userId)).thenReturn(user);
        when(validateOrderMethod.validateProduct(productId)).thenReturn(product);
        doNothing().when(validateOrderMethod).validateStock(anyInt(), eq(product));
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(OrderRmqDto.class));

        List<String> result = orderService.createOrder(requestList, userId);

        Assertions.assertEquals(requestList.size(), result.size());
        Assertions.assertEquals("Product" + productId + "상품 주문요청", result.get(0));

        verify(validateOrderMethod).validateUser(userId);
        verify(validateOrderMethod).validateProduct(productId);
        verify(validateOrderMethod).validateStock(quantity, product);
        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), any(OrderRmqDto.class));
        verify(orderCacheMethod).putOrderTag(eq(userId), anyString());

    }

    @ParameterizedTest
    @MethodSource("provideOrderFromCartTestData")
    @DisplayName("장바구니로부터 주문 생성")
    void createOrderFromCart(List<Long> cartIds) {
        Long userId = 1L;
        Long productId = 1L;
        User dummyUser = new User();
        Cart dummyCart = new Cart();
        Product dummyProduct = new Product();
        Seller dummySeller = new Seller();
        dummyCart.setQuantity(1);
        dummyCart.setProducts(Product.builder().seller(dummySeller).price(100).name("Product"+productId).build());


        when(validateOrderMethod.validateUser(anyLong())).thenReturn(dummyUser);
        when(validateOrderMethod.validateCart(anyLong(), anyLong())).thenReturn(dummyCart);
        doNothing().when(validateOrderMethod).validateStock(anyInt(), any(Product.class));
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(OrderRmqDto.class));

        List<String> result = orderService.createOrderFromCart(cartIds, userId);

        // then
        assertEquals(cartIds.size(), result.size());
        Assertions.assertEquals("Product" + productId + "상품 주문요청", result.get(0));

        verify(validateOrderMethod).validateUser(userId);
        verify(rabbitTemplate,  times(cartIds.size())).convertAndSend(anyString(), anyString(), any(OrderRmqDto.class));
        verify(orderCacheMethod,  times(cartIds.size())).putOrderTag(anyLong(), anyString());
    }


    @Test
    void getPurchasedOrderList() {
    }

    @Test
    void getSellerOrderList() {
    }

    @Test
    void getOrderList() {
    }

    @Test
    void getOrderListByCursor() {
    }

    @Test
    void deleteOne() {
    }

    @Test
    void getOrderListFromCart() {
    }

    @Test
    void getOrderListFromProduct() {
    }

    @Test
    void getKoreanTime() {
    }
}