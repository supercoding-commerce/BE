package com.github.commerce.service.chat;

import com.github.commerce.entity.collection.Chat;
import com.github.commerce.repository.chat.ChatRepository;
import com.github.commerce.repository.chat.ChatRepositoryCustomImpl;
import com.github.commerce.service.chat.exception.ChatErrorCode;
import com.github.commerce.service.chat.exception.ChatException;
import com.github.commerce.web.dto.chat.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRepositoryCustomImpl chatRepositoryCustom;
    public ChatDto getChatRoom(String customRoomId){
        return ChatDto.fromEntity(chatRepository.findByCustomRoomId(customRoomId).orElseThrow(()->new ChatException(ChatErrorCode.DEPRECATED_CHAT)));
    };


    public List<ChatDto> getUserChatList(Long userId) {
        List<Chat> chatList = chatRepositoryCustom.getUserChatsWithKey1(userId);
        return chatList.stream().map(ChatDto::fromEntity).collect(Collectors.toList());
    }
}
