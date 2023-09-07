package com.github.commerce.service.chat;

import com.github.commerce.entity.collection.Chat;
import com.github.commerce.repository.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public List<Chat> getSellerChats(Long sellerId) {
        return chatRepository.findBySellerId(sellerId);
    }

    public List<Chat> getUserChats(Long userId) {
        return chatRepository.findByUserId(userId);
    }
}
