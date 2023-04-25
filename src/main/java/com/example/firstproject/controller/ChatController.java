package com.example.firstproject.controller;

import com.example.firstproject.dto.ChatMessageDTO;
import com.example.firstproject.dto.ChatMessageResponseDTO;
import com.example.firstproject.entity.ChatUserList;
import com.example.firstproject.repository.ChatRepository;
import com.example.firstproject.repository.ChatUserListRepository;
import com.example.firstproject.service.ChatRoomService;
import com.example.firstproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import static com.example.firstproject.dto.ChatMessageDTO.MessageType.ENTER;
import static com.example.firstproject.dto.ChatMessageDTO.MessageType.TALK;
import static java.time.format.DateTimeFormatter.ofLocalizedDateTime;

@Controller
@Log4j2
@CrossOrigin("*")
@RequiredArgsConstructor
public class ChatController {

    private final ChatUserListRepository chatUserListRepository;

    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;


    @MessageMapping("/hello/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessageResponseDTO greeting(ChatMessageDTO message, @DestinationVariable("roomId") String roomId) throws Exception {
        chatService.saveChat(message);
        if (message.getType() == ENTER) {
            chatRoomService.insertUserList(message);
        }
        return new ChatMessageResponseDTO(message);
    }





}

