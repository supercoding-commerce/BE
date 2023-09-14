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
import com.github.commerce.service.product.exception.ProductErrorCode;
import com.github.commerce.service.product.exception.ProductException;
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
        return ChatDto.fromEntity(chatRepository.findByCustomRoomId(customRoomId).orElseThrow(()->new ChatException(ChatErrorCode.DEPRECATED_CHAT)));
    };


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
