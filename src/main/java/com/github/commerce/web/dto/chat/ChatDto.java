package com.github.commerce.web.dto.chat;

import com.github.commerce.entity.collection.Chat;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
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

    private String imageUrl;

    private String productName;

    private List<Chat.Message> chats;

    private Chat.Message lastChat;
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

    private static Chat.Message getLastChat(List<Chat.Message> chats){

        return chats.stream()
                .max(Comparator.comparing(message -> LocalDateTime.parse(message.getTimestamp(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .orElse(null);
    }
}
