package com.example.firstproject.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {

    public enum MessageType {
        ENTER, TALK, LEAVE
    }
    private String room_id;
    private MessageType type;
    private String sender;
    private String message;


}

