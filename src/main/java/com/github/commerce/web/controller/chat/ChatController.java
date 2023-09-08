package com.github.commerce.web.controller.chat;

import com.github.commerce.entity.collection.Chat;
import com.github.commerce.repository.user.UserDetailsImpl;
import com.github.commerce.service.chat.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Api(tags = "채팅 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/api/chat")
@RestController
public class ChatController {
    private final ChatService chatService;

//    @GetMapping("/seller/{sellerId}")
//    public ResponseEntity<List<Chat>> getSellerChats(
//            @PathVariable Long sellerId
//    ){
//        return ResponseEntity.ok(chatService.getSellerChats(sellerId));
//    }
    @ApiOperation(value = "채팅방 목록 조회, 로그인필요")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @GetMapping
    public ResponseEntity<List<Chat>> getUserChats(
            @AuthenticationPrincipal UserDetailsImpl userDetails

    ){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(chatService.getUserChatList(userId));
    }

    @ApiOperation(value = "채팅방 채팅내역 상세조회, 로그인필요")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = String.class),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @GetMapping("/{customRoomId}")
    public ResponseEntity<Chat> getChatRoom(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable String customRoomId
    ){
        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(chatService.getChatRoom(customRoomId));
    }
}
