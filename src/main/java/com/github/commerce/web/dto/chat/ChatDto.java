package com.github.commerce.web.dto.chat;

import com.github.commerce.entity.Cart;
import com.github.commerce.entity.Product;
import com.github.commerce.entity.collection.Chat;
import com.github.commerce.web.dto.cart.CartDto;
import lombok.*;

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
}
