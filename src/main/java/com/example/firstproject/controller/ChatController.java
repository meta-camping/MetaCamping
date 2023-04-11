package com.example.firstproject.controller;

import com.example.firstproject.dto.ChatMessageDTO;
import com.example.firstproject.dto.ChatMessageResponseDTO;
<<<<<<< HEAD
import com.example.firstproject.entity.ChatUserList;
import com.example.firstproject.repository.ChatRepository;
import com.example.firstproject.repository.ChatUserListRepository;
import com.example.firstproject.service.ChatRoomService;
=======
import com.example.firstproject.repository.ChatRepository;
>>>>>>> 5b38ca2db68537814b28a1547cbdb713040b6742
import com.example.firstproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.util.HtmlUtils;

import static com.example.firstproject.dto.ChatMessageDTO.MessageType.ENTER;
import static com.example.firstproject.dto.ChatMessageDTO.MessageType.TALK;

@Controller
@Log4j2
@CrossOrigin("*")
@RequiredArgsConstructor
public class ChatController {
<<<<<<< HEAD
    private final ChatUserListRepository chatUserListRepository;

    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
=======

    private final ChatRepository chatRepository;
    private final ChatService chatService;
>>>>>>> 5b38ca2db68537814b28a1547cbdb713040b6742


    @MessageMapping("/hello/{room_id}")
    @SendTo("/topic/{room_id}")
    public ChatMessageResponseDTO greeting(ChatMessageDTO message, @DestinationVariable("room_id") String room_id) throws Exception {
        //Thread.sleep(1000); // simulated delay
        if (message.getType() == ENTER) {
            chatService.saveChat(message);
<<<<<<< HEAD
            chatRoomService.insertUserList(message);
=======
>>>>>>> 5b38ca2db68537814b28a1547cbdb713040b6742
            return new ChatMessageResponseDTO(HtmlUtils.htmlEscape(message.getSender()) + "님이 입장했습니다.");
        }
        if (message.getType() == TALK) {
            chatService.saveChat(message);
            return new ChatMessageResponseDTO(HtmlUtils.htmlEscape(message.getSender()) + ":" + message.getMessage());
        }
        return null;
    }



}

