package com.example.firstproject.service;

import com.example.firstproject.dto.ChatMessageDTO;
import com.example.firstproject.entity.ChatMessage;
import com.example.firstproject.entity.ChatRoom;
import com.example.firstproject.repository.ChatRepository;
import com.example.firstproject.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;

    private final ChatRepository chatRepository;


    public ChatMessage saveChat(ChatMessageDTO messageDTO) {
        ChatMessage chatMessage = new ChatMessage(messageDTO);
        ChatRoom chatRoom = chatRoomRepository.findById(messageDTO.getRoom_id())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 ChatRoom이 없습니다."));
        // ChatRoom의 updateTime을 수정합니다.
        chatRoom.update(LocalDateTime.now());
        chatRoomRepository.save(chatRoom);
        return chatRepository.save(chatMessage);

    }
}

