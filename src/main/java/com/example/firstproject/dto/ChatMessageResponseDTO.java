package com.example.firstproject.dto;

import java.time.LocalDateTime;

public class ChatMessageResponseDTO {

    private String sender;
    private String content;
    private String send_time;


    public ChatMessageResponseDTO(String send_time, String sender, String content) {
        this.send_time = send_time;
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getSend_time() {
        return send_time;
    }

    public String getContent() {
        return content;
    }
}
