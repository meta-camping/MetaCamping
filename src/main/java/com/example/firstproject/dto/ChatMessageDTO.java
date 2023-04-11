package com.example.firstproject.dto;

import lombok.Data;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;


@Data
public class ChatMessageDTO {
<<<<<<< HEAD

=======
>>>>>>> 5b38ca2db68537814b28a1547cbdb713040b6742

    public enum MessageType {
        ENTER, TALK, LEAVE
    }
    private String room_id;
    private MessageType type;
    private String sender;
    private String message;

}

