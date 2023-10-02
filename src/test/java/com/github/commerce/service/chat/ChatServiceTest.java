package com.github.commerce.service.chat;

import com.github.commerce.entity.collection.Chat;
import com.github.commerce.repository.chat.ChatRepository;
import com.github.commerce.repository.chat.ChatRepositoryCustomImpl;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.user.SellerRepository;
import com.github.commerce.service.chat.exception.ChatException;
import com.github.commerce.web.dto.chat.ChatDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@TestPropertySource(locations = "classpath:application-test.yml")
@RunWith(MockitoJUnitRunner.class)  // @Mock 사용을 위해 설정
class ChatServiceTest {
    @InjectMocks
    private ChatService chatService;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private ChatRepositoryCustomImpl chatRepositoryCustom;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private SellerRepository sellerRepository;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getChatRoom() {
        Chat mockChat = new Chat();
        Map<String, Map<String, String>> chats = new HashMap<>();
        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("test", "test");
        chats.put("2023-09-28T20:15:30Z", innerMap);  // 예를 들면 이런 ISO 형식의 문자열을 사용
        chats.put("2023-09-28T19:15:30Z", innerMap);  // 정렬을 확인하기 위해 두 개의 다른 날짜/시간 추가
        mockChat.setChats(chats);

        String customRoomId = "testRoom";
        mockChat.setCustomRoomId(customRoomId);

        when(chatRepository.findByCustomRoomId(customRoomId)).thenReturn(Optional.of(mockChat));
        //when(chatService.sortChatsByDate(anyMap())).thenReturn(anyMap());

        ChatDto chatRoom = chatService.getChatRoom(customRoomId);

        assertNotNull(chatRoom);
        verify(chatRepository).findByCustomRoomId(customRoomId);
        //verify(chatService).sortChatsByDate(anyMap());

        List<String> sortedKeys = new ArrayList<>(chatRoom.getChats().keySet());
        assertTrue(sortedKeys.get(0).compareTo(sortedKeys.get(1)) < 0);  // 키가 올바르게 정렬되었는지 확인


    }

    @Test
    void getSellerChatListTest() {
        Long sellerId = 1L;
        Long productId = 1L;
        String mockedShopImageUrl = "http://example.com/shop-image.jpg";
        List<Chat> mockedChatList = new ArrayList<>();

        when(chatService.getSellerImage(sellerId)).thenReturn(mockedShopImageUrl);
        when(chatRepositoryCustom.getSellerChatList(sellerId, productId)).thenReturn(mockedChatList);
        when(chatService.getProductImageAndName(anyLong())).thenReturn(createMockedProductInfo());

        Map<String, Object> resultMap = chatService.getSellerChatList(sellerId, productId);

        assertEquals(mockedShopImageUrl, resultMap.get("shopImage"));
        assertNotNull(resultMap.get("chatList"));
    }


    private Map<String, String> createMockedProductInfo() {
        Map<String, String> productInfo = new HashMap<>();
        productInfo.put("name", "Test Product");
        productInfo.put("url", "http://example.com/product-image.jpg");
        return productInfo;
    }

    @Test
    void getUserChatList() {
    }

    @Test
    void cleanupOldChats() {
    }
}