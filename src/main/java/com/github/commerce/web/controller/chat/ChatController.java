package com.github.commerce.web.controller.chat;

import com.github.commerce.entity.collection.Chat;
import com.github.commerce.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/chat")
@RestController
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Chat>> getSellerChats(
            @PathVariable Long sellerId
    ){
        return ResponseEntity.ok(chatService.getSellerChats(sellerId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Chat>> getUserChats(
            @PathVariable Long userId
    ){
        return ResponseEntity.ok(chatService.getUserChats(userId));
    }
}
