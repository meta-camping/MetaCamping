package com.example.firstproject.dto;

import lombok.Data;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;


@Data
public class ChatMessageDTO<T> implements Message<T> {


    public enum MessageType {
        ENTER, TALK, LEAVE
    }
    private String room_id;
    private MessageType type;
    private String sender;
    private String message;
    private String created_time;
    private T payload;
    private T headers;


    @Override
    public T getPayload() {
        return payload;
    }

    @Override
    public MessageHeaders getHeaders() {
        return (MessageHeaders) headers;
    }

}

