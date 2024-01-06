package com.github.commerce.entity.collection;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chat")
public class Chat {
    @Id // MongoDB ObjectId
    private String chatId;

    private String customRoomId;

    private Long sellerId;

    private Long productId;

    private Long userId;

    private String shopName;

    private String userName;

    private List<Message> chats;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Message {
        private String timestamp; // 메시지의 타임스탬프
        private String sender;    // 메시지의 보낸 사람
        private String content;   // 메시지 내용
    }
}