package com.example.firstproject.entity;

import com.example.firstproject.dto.ChatMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage {

    public enum MessageType {
        ENTER, TALK, LEAVE
    }
    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private String message_id;
    @Column(name = "room_id")
    private String room_id;
    @Column(name = "type")
    private ChatMessageDTO.MessageType type;
    //채팅방 ID
    //보내는 사람
    @Column(name = "member_id")
    private String sender;
    //내용
    @Column(name = "message")
    private String message;
    @CreatedDate
    @Column(name = "created_time")
    private String created_time;

    public ChatMessage(ChatMessageDTO messageDTO) {
        this.room_id = messageDTO.getRoom_id();
        this.message = messageDTO.getMessage();
        this.type = messageDTO.getType();
        this.sender = messageDTO.getSender();
        this.created_time = LocalDate.now().toString();
    }
}
