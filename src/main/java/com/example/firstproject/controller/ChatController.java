package com.example.firstproject.controller;

import com.example.firstproject.dto.ChatBeforeMessageResponseDTO;
import com.example.firstproject.dto.ChatMessageRequestDTO;
import com.example.firstproject.dto.ChatMessageResponseDTO;
import com.example.firstproject.entity.ChatMessage;
import com.example.firstproject.repository.ChatRepository;
import com.example.firstproject.repository.ChatUserListRepository;
import com.example.firstproject.service.ChatRoomService;
import com.example.firstproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.firstproject.dto.ChatMessageRequestDTO.MessageType.ENTER;

@Controller
@Log4j2
@CrossOrigin("*")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessageResponseDTO chatting(ChatMessageRequestDTO message, @DestinationVariable("roomId") String roomId) throws Exception {
        chatService.saveChat(message);
        if (message.getType() == ENTER) {
            chatRoomService.insertUserList(message);
        }
        return new ChatMessageResponseDTO(message);
    }

    @GetMapping("/api/chat/room/{roomId}/{memberId}/before-messages")
    @ResponseBody
    public ResponseEntity<List<ChatBeforeMessageResponseDTO>> beforeMessages(@PathVariable String roomId, @PathVariable String memberId) {
        List<ChatBeforeMessageResponseDTO> beforeMessages = chatRoomService.ChatBeforeMessages(roomId,memberId);
        return ResponseEntity.ok(beforeMessages);
    }
}

