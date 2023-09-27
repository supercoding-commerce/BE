package com.github.commerce.service.order;

import com.github.commerce.entity.*;
import com.github.commerce.repository.order.OrderRepository;
import com.github.commerce.service.order.util.OrderCacheMethod;
import com.github.commerce.service.order.util.ValidateOrderMethod;
import com.github.commerce.web.dto.order.OrderDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        // Arrange
        // Arrange
        Long userId = 1L;

        Product mockProduct = new Product();
        mockProduct.setId(100L);
        mockProduct.setName("Mock Product");
        Seller mockSeller = new Seller();
        mockSeller.setShopName("Mock Shop");
        mockProduct.setSeller(mockSeller);

        Order mockOrder1 = new Order();
        mockOrder1.setCreatedAt(LocalDateTime.of(2023, 9, 26, 12, 0));
        mockOrder1.setProducts(mockProduct);

        Order mockOrder2 = new Order();
        mockOrder2.setCreatedAt(LocalDateTime.of(2023, 9, 26, 15, 0));
        mockOrder2.setProducts(mockProduct);

        when(orderRepository.findPaidOrderByUserIdSortByCreatedAtDesc(userId))
                .thenReturn(Arrays.asList(mockOrder1, mockOrder2));

        // Act
        List<Map<LocalDate, List<OrderDto>>> result = orderService.getPurchasedOrderList(userId);

        // Assert
        assertEquals(1, result.size());
        Map<LocalDate, List<OrderDto>> ordersMap = result.get(0);
        assertEquals(1, ordersMap.size());
        assertEquals(2, ordersMap.get(LocalDate.of(2023, 9, 26)).size());
    }

    @Test
    void getSellerOrderList() {
        // Arrange
        Long userId = 1L;
        Product mockProduct = new Product();
        mockProduct.setId(100L);
        mockProduct.setName("Mock Product");
        Seller mockSeller = new Seller();
        mockSeller.setShopName("Mock Shop");
        mockSeller.setId(2L);
        mockProduct.setSeller(mockSeller);


        when(validateOrderMethod.validateSellerByUserId(userId)).thenReturn(mockSeller);

        Order mockOrder1 = new Order();
        mockOrder1.setCreatedAt(LocalDateTime.of(2023, 9, 26, 12, 0));
        mockOrder1.setProducts(mockProduct);

        Order mockOrder2 = new Order();
        mockOrder2.setCreatedAt(LocalDateTime.of(2023, 9, 26, 15, 0));
        mockOrder2.setProducts(mockProduct);

        when(orderRepository.findPaidOrderBySellerIdSortByCreatedAtDesc(mockSeller.getId()))
                .thenReturn(Arrays.asList(mockOrder1, mockOrder2));

        // Act
        List<Map<LocalDate, List<OrderDto>>> result = orderService.getSellerOrderList(userId);

        // Assert
        assertEquals(1, result.size());
        Map<LocalDate, List<OrderDto>> ordersMap = result.get(0);
        assertEquals(1, ordersMap.size());
        assertEquals(2, ordersMap.get(LocalDate.of(2023, 9, 26)).size());
    }

    @Test
    void getOrderList() {
        // Arrange
        Long userId = 1L;
        User mockUser = new User();
        String mockOrderTag = "testTag";

        Product mockProduct = new Product();
        mockProduct.setId(100L);
        mockProduct.setName("Mock Product");
        Seller mockSeller = new Seller();
        mockSeller.setShopName("Mock Shop");
        mockProduct.setSeller(mockSeller);

        Order mockOrder1 = new Order();
        mockOrder1.setProducts(mockProduct);
        Order mockOrder2 = new Order();
        mockOrder2.setProducts(mockProduct);

        when(validateOrderMethod.validateUser(userId)).thenReturn(mockUser);
        when(orderCacheMethod.getOrderTag(userId)).thenReturn(mockOrderTag);
        when(orderRepository.findByUsersIdAndOrderTagAndOrderState(userId, mockOrderTag, 1))
                .thenReturn(Arrays.asList(mockOrder1, mockOrder2));

        // Act
        List<OrderDto> result = orderService.getOrderList(userId);

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getOrderListByCursor() {
        // Arrange
        Long userId = 1L;
        Long cursorId = 100L;
        int pageSize = 10;

        Product mockProduct = new Product();
        mockProduct.setId(100L);
        mockProduct.setName("Mock Product");
        Seller mockSeller = new Seller();
        mockSeller.setShopName("Mock Shop");
        mockProduct.setSeller(mockSeller);

        Order mockOrder1 = new Order();
        mockOrder1.setProducts(mockProduct);
        Order mockOrder2 = new Order();
        mockOrder2.setProducts(mockProduct);

        List<Order> mockOrderList = Arrays.asList(mockOrder1, mockOrder2);
        Page<Order> mockOrderPage = new PageImpl<>(mockOrderList, PageRequest.of(0, pageSize), mockOrderList.size());

        when(orderRepository.findAllByUsersIdAndCursorId(userId, cursorId, PageRequest.of(0, pageSize)))
                .thenReturn(mockOrderPage);

        // Act
        Page<OrderDto> result = orderService.getOrderListByCursor(userId, cursorId);

        // Assert
        assertEquals(2, result.getContent().size());
    }

    @Test
    void deleteOne() {
        // Arrange
        Long userId = 1L;
        Long orderId = 2L;

        Order validatedOrder = new Order();
        validatedOrder.setId(orderId);

        when(validateOrderMethod.validateOrder(orderId, userId)).thenReturn(validatedOrder);

        // Act
        String result = orderService.deleteOne(orderId, userId);

        // Assert
        verify(validateOrderMethod).validateUser(userId);
        verify(validateOrderMethod).validateOrder(orderId, userId);
        verify(orderRepository).deleteById(orderId);

        String expectedMessage = orderId + "번 주문 삭제";
        assertEquals(expectedMessage, result);
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