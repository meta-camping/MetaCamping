package com.example.firstproject.entity;

import com.example.firstproject.dto.ChatMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private long message_id;
    @Column(name = "room_id")
    private String room_id;
    @Column(name = "type")
    private ChatMessageDTO.MessageType type;
    //채팅방 ID
    //보내는 사람
    @Column(name = "member_id")
    private String member_id;
    //내용
    @Column(name = "message")
    private String message;
    @CreatedDate
    @Column(name = "created_time")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_time;

    public ChatMessage(ChatMessageDTO messageDTO) {
        this.room_id = messageDTO.getRoom_id();
        this.message = messageDTO.getMessage();
        this.type = messageDTO.getType();
        this.member_id = messageDTO.getSender();
        this.created_time = LocalDateTime.now();
    }
}
