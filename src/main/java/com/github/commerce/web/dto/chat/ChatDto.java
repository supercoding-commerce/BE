package com.github.commerce.web.dto.chat;

import com.github.commerce.entity.collection.Chat;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDto {

    private String chatId;

    private String customRoomId;

    private Long sellerId;

    private Long productId;

    private Long userId;

    private String shopName;

    private String userName;

    private String imageUrl;

    private String productName;

    private Map<String, Map<String, String>> chats;

    private Map<String, String> lastChat;
    public static ChatDto fromEntity(Chat chat){

        return ChatDto.builder()
                .chatId(chat.getChatId())
                .customRoomId(chat.getCustomRoomId())
                .productId(chat.getProductId())
                .userId(chat.getUserId())
                .sellerId(chat.getSellerId())
                .shopName(chat.getShopName())
                .userName(chat.getUserName())
                .chats(chat.getChats())
                .build();
    }

    public static ChatDto fromEntityList(Chat chat, String productImage, String produdctName){
        return ChatDto.builder()
                .chatId(chat.getChatId())
                .customRoomId(chat.getCustomRoomId())
                .productId(chat.getProductId())
                .userId(chat.getUserId())
                .sellerId(chat.getSellerId())
                .shopName(chat.getShopName())
                .userName(chat.getUserName())
                .imageUrl(productImage)
                .productName(produdctName)
                .lastChat(getLastChat(chat.getChats()))
                .build();
    }

    private static Map<String, String> getLastChat(Map<String, Map<String, String>> chat){
        String lastKey = null;
        LocalDateTime lastDateTime = null;

        for (String key : chat.keySet()) {
            // 앞의 19글자만 추출하여 LocalDateTime으로 변환
            String keyDateTimeStr = key.substring(0, 19);
            LocalDateTime keyDateTime = LocalDateTime.parse(keyDateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            if (lastDateTime == null || keyDateTime.isAfter(lastDateTime)) {
                lastDateTime = keyDateTime;
                lastKey = key;
            }
        }

        if (lastKey != null) {
            return chat.get(lastKey);
        } else {
            return null;
        }
    }
}
