package com.example.firstproject.service;

import com.example.firstproject.dto.ChatMessageDTO;
import com.example.firstproject.entity.ChatMessage;
import com.example.firstproject.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public static ChatMessage saveChat(ChatMessageDTO messageDTO) {
        ChatMessage chatMessage = new ChatMessage(messageDTO);
        return chatMessage;
    }
}

