package com.github.commerce.service.chat;

import com.github.commerce.entity.Product;
import com.github.commerce.entity.Seller;
import com.github.commerce.entity.collection.Chat;
import com.github.commerce.repository.chat.ChatRepository;
import com.github.commerce.repository.chat.ChatRepositoryCustomImpl;
import com.github.commerce.repository.product.ProductRepository;
import com.github.commerce.repository.user.SellerRepository;
import com.github.commerce.service.chat.exception.ChatErrorCode;
import com.github.commerce.service.chat.exception.ChatException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import com.github.commerce.web.dto.chat.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRepositoryCustomImpl chatRepositoryCustom;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    public ChatDto getChatRoom(String customRoomId){
          Chat chatEntity = chatRepository.findByCustomRoomId(customRoomId).orElseThrow(()->new ChatException(ChatErrorCode.DEPRECATED_CHAT));
          Map<String, Map<String, String>> chats = chatEntity.getChats();

          Map<String, Map<String, String>> sortedChats = chats.entrySet()
                .stream()
                .sorted((entry1, entry2) -> {
                    String key1DateTimeStr = entry1.getKey().substring(0, 19);
                    String key2DateTimeStr = entry2.getKey().substring(0, 19);
                    LocalDateTime date1 = LocalDateTime.parse(key1DateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    LocalDateTime date2 = LocalDateTime.parse(key2DateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    return date1.compareTo(date2);

                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        chatEntity.setChats(sortedChats);
        return ChatDto.fromEntity(chatEntity);

    };

    public List<ChatDto> getSellerChatList(Long sellerId, Long productId) {
        List<Chat> chatList = chatRepositoryCustom.getSellerChatList(sellerId, productId);
        List<ChatDto> resultList = new ArrayList<>();
        chatList.forEach(chat -> {
            Map<String,String> productInfo = getProductImageAndName(chat.getProductId());
            String productName = productInfo.get("name");
            String productImage = productInfo.get("url");
            resultList.add(ChatDto.fromEntityList(chat, productImage, productName));
        });
        return resultList;
    }

    public List<ChatDto> getUserChatList(Long userId, Long sellerId) {
        //Seller seller = sellerRepository.findById(sellerId).orElseThrow(null);

        List<Chat> chatList = chatRepositoryCustom.getUserChatList(userId, sellerId);
        List<ChatDto> resultList = new ArrayList<>();
        chatList.forEach(chat -> {
           Map<String,String> productInfo = getProductImageAndName(chat.getProductId());
           String productName = productInfo.get("name");
           String productImage = productInfo.get("url");
            resultList.add(ChatDto.fromEntityList(chat, productImage, productName));
        });
        return resultList;
    }

    private Map<String, String> getProductImageAndName(Long productId){
        Optional<Product> productOptional = productRepository.findById(productId);
        Map<String, String> result = new HashMap<>();

        if (productOptional.isPresent()) {
            String urlList = productOptional.get().getThumbnailUrl();
            String productName = productOptional.get().getName();
            String[] urls = urlList.split(",");

            if (urls.length > 0) {
                result.put("url", urls[0]);
            }

            result.put("name", productName);
        }

        return result.isEmpty() ? null : result;
    }


}
