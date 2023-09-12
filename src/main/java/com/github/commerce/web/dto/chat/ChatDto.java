package com.github.commerce.web.dto.chat;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.collection.Chat;
import com.github.commerce.web.dto.cart.CartDto;
import lombok.*;

import java.util.Collections;
import java.util.List;
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

    private Map<Integer, Map<String, String>> chats;
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

    public static ChatDto fromEntityList(Chat chat){
        return ChatDto.builder()
                .chatId(chat.getChatId())
                .customRoomId(chat.getCustomRoomId())
                .productId(chat.getProductId())
                .userId(chat.getUserId())
                .sellerId(chat.getSellerId())
                .shopName(chat.getShopName())
                .userName(chat.getUserName())
                .chats(getLastChat(chat.getChats()))
                .build();
    }

    private static Map<Integer, Map<String, String>> getLastChat(Map<Integer, Map<String, String>> chat){
        int lastKey = Collections.max(chat.keySet());
        Map<String, String> lastKeyMap = chat.get(lastKey);
        return Collections.singletonMap(lastKey, lastKeyMap);
    }
}
