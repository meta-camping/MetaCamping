package com.example.firstproject.dto;

import java.time.LocalDateTime;

public class ChatMessageResponseDTO {

    private String sender;
    private String message;
    private LocalDateTime createTime;


    public ChatMessageResponseDTO(ChatMessageDTO message) {
        this.createTime = message.getCreateTime();
        this.sender = message.getSender();
        this.message = message.getMessage();
    }

    public String getSender() {
        return sender;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public String getMessage() {
        return message;
    }
}
