package com.github.commerce.service.cart;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.Seller;
import com.github.commerce.entity.User;
import com.github.commerce.repository.cart.CartRepository;
import com.github.commerce.service.cart.util.ValidatCartMethod;
import com.github.commerce.web.dto.cart.CartDto;
import com.github.commerce.web.dto.cart.CartRmqDto;
import com.github.commerce.web.dto.cart.PostCartDto;
import com.github.commerce.web.dto.cart.PutCartDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@TestPropertySource(locations = "classpath:application-test.yml")
@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정
class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private ValidatCartMethod validatCartMethod;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private CartRepository cartRepository;

    static Stream<Arguments> provideCartTestData() {
        return Stream.of(
                Arguments.of(1L, 2),
                Arguments.of(2L, 3),
                Arguments.of(3L, 4)
        );
    }

    static Stream<Arguments> provideCartTestDataForModification() {
        return Stream.of(
                Arguments.of(1L, 1L, 2, Arrays.asList("Option1")),
                Arguments.of(2L, 2L, 3, Arrays.asList("Option2")),
                Arguments.of(3L, 3L, 4, Arrays.asList("Option3"))
        );
    }

//This is redundant when using @Mock along with @ExtendWith(MockitoExtension.class). You should choose one approach and stick with it.
//    @BeforeEach
//    public void setup() {
//        System.out.println("각각의 테스트 실행 전 수행");
//        MockitoAnnotations.openMocks(this);
//    }

    @BeforeAll  // static으로 만들어야 한다
    static void beforeAll() {
        System.out.println("모든 테스트 실행 전 최초로 수행");
    }

    @AfterAll // static으로 만들어야 한다
    static void afterAll() {
        System.out.println("모든 테스트 실행 후 마지막으로 수행");
    }

    @AfterEach
    void tearDown() {
        System.out.println("각각 테스트 실행 후 수행\n"); }


    @Test
    @DisplayName("모든 장바구니 가져오기")
    void getAllCarts() {
        // given
        Long userId = 1L;
        Cart cart = new Cart();
        cart.setCreatedAt(LocalDateTime.of(2022, 9, 26, 12, 0));
        cart.setProducts(Product.builder().price(1000).seller(new Seller()).build());
        cart.setUsers(new User());
        cart.setQuantity(1);

        List<Cart> cartList = Collections.singletonList(cart);
        when(cartRepository.findAllByUsersIdOrderByCreatedAtDesc(userId)).thenReturn(cartList);

        // when
        List<Map<LocalDate, List<CartDto>>> result = cartService.getAllCarts(userId);

        // then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.get(0).size());
        Assertions.assertTrue(result.get(0).containsKey(LocalDate.of(2022, 9, 26)));
    }

    @Test
    @DisplayName("커서를 사용하여 장바구니 가져오기")
    void getAllCartWithCursor() {
        // given
        Long userId = 1L;
        Long cursorId = 2L;
        int pageSize = 10;
        Cart cart = new Cart();
        cart.setCreatedAt(LocalDateTime.of(2022, 9, 26, 12, 0));
        cart.setProducts(Product.builder().price(1000).seller(new Seller()).build());
        cart.setUsers(new User());
        cart.setQuantity(1);

        Page<Cart> page = new PageImpl<>(Collections.singletonList(cart), PageRequest.of(0, pageSize), 1);
        when(cartRepository.findAllByUserId(userId, cursorId, PageRequest.of(0, pageSize))).thenReturn(page);

        // when
        Page<CartDto> result = cartService.getAllCartWithCursor(userId, cursorId);

        // then
        Assertions.assertEquals(1, result.getTotalElements());
    }

    @ParameterizedTest
    @MethodSource("provideCartTestData")
    //@ValueSource(longs = {1L, 2L, 3L}) 이 방식은 하나의 인자에 대해 여러 값을 제공할 뿐이다.
    @DisplayName("장바구니 추가")
    void addToCart(Long productId, Integer quantity) {
        PostCartDto.PostCartRequest request = new PostCartDto.PostCartRequest(productId, quantity, Arrays.asList("Option1"));
        List<PostCartDto.PostCartRequest> requestList = new ArrayList<>();
        requestList.add(request);

        Long userId = 1L;
        User user = new User();
        Product product = new Product();
        product.setName("Product"+productId);

        // Mock 객체의 메소드가 호출될 때 어떤 행동을 할지 지정합니다.
        when(validatCartMethod.validateUser(userId)).thenReturn(user);
        when(validatCartMethod.validateProduct(productId)).thenReturn(product);
        doNothing().when(validatCartMethod).validateDuplicateCart(any(), anyLong(), anyString());
        doNothing().when(validatCartMethod).validateStock(anyInt(), any());
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(CartRmqDto.class));

        // When
        List<String> result = cartService.addToCart(requestList, userId);

        // Then
        Assertions.assertEquals(requestList.size(), result.size());
        Assertions.assertEquals("Product"+productId+"상품을 장바구로 추가합니다.", result.get(0));

        //Mock 객체의 메소드가 특정 방식으로 호출되었는지 검증합니다.
        verify(validatCartMethod).validateUser(userId);
        verify(validatCartMethod).validateProduct(productId);
        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), any(CartRmqDto.class));
    }

    @ParameterizedTest
    @MethodSource("provideCartTestDataForModification")
    @DisplayName("장바구니 수정")
    void modifyCart(Long cartId, Long productId, Integer quantity, List<String> options) {
        PutCartDto.PutCartRequest request = new PutCartDto.PutCartRequest(cartId, productId, quantity, options);
        List<PutCartDto.PutCartRequest> requestList = new ArrayList<>();
        requestList.add(request);

        Long userId = 1L;
        User user = new User();
        Product product = new Product();
        product.setName("Product" + productId);
        Cart cart = new Cart();

        when(validatCartMethod.validateUser(userId)).thenReturn(user);
        when(validatCartMethod.validateCart(cartId, userId)).thenReturn(cart);
        when(validatCartMethod.validateProduct(productId)).thenReturn(product);
        doNothing().when(validatCartMethod).validateStock(anyInt(), any());
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(CartRmqDto.class));

        List<String> result = cartService.modifyCart(requestList, userId);

        Assertions.assertEquals(requestList.size(), result.size());
        Assertions.assertEquals("Product" + productId + "상품을 장바구니서 수정합니다.", result.get(0));

        verify(validatCartMethod).validateUser(userId);
        verify(validatCartMethod).validateCart(cartId, userId);
        verify(validatCartMethod).validateProduct(productId);
        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), any(CartRmqDto.class));
    }

    @Test
    @DisplayName("모든 장바구니 삭제")
    void deleteAll() {
        // given
        Long userId = 1L;
        User user = new User();
        user.setUserName("TestUser");
        when(validatCartMethod.validateUser(userId)).thenReturn(user);

        // when
        String result = cartService.deleteAll(userId);

        // then
        Assertions.assertEquals("TestUser님의 장바구니 목록이 삭제되었습니다.", result);
        verify(cartRepository).deleteAllByUsersId(userId);
    }
    @Test
    @DisplayName("특정 장바구니 삭제")
    void deleteOne() {
        // given
        Long userId = 1L;
        Long cartId = 1L;
        Cart cart = new Cart();
        cart.setId(cartId);
        when(validatCartMethod.validateUser(userId)).thenReturn(new User());
        when(validatCartMethod.validateCart(cartId, userId)).thenReturn(cart);

        // when
        String result = cartService.deleteOne(cartId, userId);

        // then
        Assertions.assertEquals("1번 장바구니 삭제", result);
        verify(cartRepository).deleteById(cartId);
    }

}