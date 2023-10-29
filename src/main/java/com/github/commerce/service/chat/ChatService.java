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

import com.github.commerce.service.user.exception.UserException;
import com.github.commerce.web.dto.chat.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRepositoryCustomImpl chatRepositoryCustom;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    @Transactional
    public ChatDto getChatRoom(String customRoomId){
          Chat chatEntity = chatRepository.findByCustomRoomId(customRoomId).orElseThrow(()->new ChatException(ChatErrorCode.ROOM_NOT_FOUND));
          Map<String, Map<String, String>> chats = chatEntity.getChats();

        Map<String, Map<String, String>> sortedChats = sortChatsByDate(chats);
        chatEntity.setChats(sortedChats);

        return ChatDto.fromEntity(chatEntity);
    };


    @Transactional
    public Map<String, Object> getSellerChatList(Long sellerId, Long productId) {

        String shopImageUrl = getSellerImage(sellerId);

        List<Chat> chatList = chatRepositoryCustom.getSellerChatList(sellerId, productId);
        List<ChatDto> resultList = new ArrayList<>();
        chatList.forEach(chat -> {
            if(chat.getChats() == null || chat.getChats().isEmpty()){
                return;
            }
            Map<String,String> productInfo = getProductImageAndName(chat.getProductId());
            String productName = productInfo.get("name");
            String productImage = productInfo.get("url");
            resultList.add(ChatDto.fromEntityList(chat, productImage, productName));
        });

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("chatList", resultList);
        resultMap.put("shopImage", shopImageUrl);
        return resultMap;
    }

    @Transactional
    public Map<String, Object> getUserChatList(Long userId, Long sellerId) {
        String shopImageUrl = getSellerImage(sellerId);

        List<Chat> chatList = chatRepositoryCustom.getUserChatList(userId, sellerId);
        List<ChatDto> resultList = new ArrayList<>();
        chatList.forEach(chat -> {
            if(chat.getChats() == null || chat.getChats().isEmpty()){
                return;
            }
           Map<String,String> productInfo = getProductImageAndName(chat.getProductId());
           String productName = productInfo.get("name");
           String productImage = productInfo.get("url");
            resultList.add(ChatDto.fromEntityList(chat, productImage, productName));
        });

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("chatList", resultList);
        resultMap.put("shopImage", shopImageUrl);
        return resultMap;
    }

    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")  // This will run at midnight every day
    public void cleanupOldChats() {
        chatRepositoryCustom.cleanupOldChats();
    }

    protected Map<String, String> getProductImageAndName(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(()-> new ChatException(ChatErrorCode.THIS_PRODUCT_DOES_NOT_EXIST));
        Map<String, String> result = new HashMap<>();


            String url = product.getThumbnailUrl();
            String productName = product.getName();
            //String[] urls = urlList.split(",");
//            if (urls.length > 0) {
//                result.put("url", urls[0]);
//            }
            result.put("url", url);
            result.put("name", productName);


        return result;
    }

    protected String getSellerImage(Long sellerId){
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(()-> new ChatException(ChatErrorCode.SELLER_NOT_FOUND));
        return seller.getShopImageUrl();
    }

    protected Map<String, Map<String, String>> sortChatsByDate(Map<String, Map<String, String>> chats) {
        return chats.entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> extractDateTimeFromKey(entry.getKey())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        //정렬된 순서대로 데이터를 유지하려면 LinkedHashMap이 필요합니다. 이렇게 하지 않으면, 정렬 순서가 Map에 저장될 때 무시될 수 있습니다.
    }

    private LocalDateTime extractDateTimeFromKey(String key) {
        String dateTimeStr = key.substring(0, 19);
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
