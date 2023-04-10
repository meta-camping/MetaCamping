package com.example.firstproject.dto;


public class ChatMessageResponseDTO {

    private String content;

    public ChatMessageResponseDTO() {
    }

    public ChatMessageResponseDTO(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}