package com.example.firstproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageDTO {

    public enum MessageType {
        ENTER, TALK, LEAVE
    }

    private String roomId;
    private MessageType type;
    private String sender;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

}



