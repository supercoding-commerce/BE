package com.github.commerce.service.chat;

import com.github.commerce.entity.collection.Chat;
import com.github.commerce.repository.chat.ChatRepository;
import com.github.commerce.repository.chat.ChatRepositoryCustomImpl;
import com.github.commerce.service.chat.exception.ChatErrorCode;
import com.github.commerce.service.chat.exception.ChatException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRepositoryCustomImpl chatRepositoryCustom;
    public Chat getChatRoom(String customRoomId){
        return chatRepository.findByCustomRoomId(customRoomId).orElseThrow(()->new ChatException(ChatErrorCode.DEPRECATED_CHAT));
    };


    public List<Chat> getUserChatList(Long userId) {
        return chatRepositoryCustom.getUserChatsWithKey1(userId);
    }
}
