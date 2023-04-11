package com.example.firstproject.entity;

import com.example.firstproject.dto.ChatMessageDTO;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
<<<<<<< HEAD
=======
import java.time.format.DateTimeFormatter;
>>>>>>> 5b38ca2db68537814b28a1547cbdb713040b6742

@Data
@RequiredArgsConstructor
@Entity
public class ChatMessage {

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
<<<<<<< HEAD
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
=======
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
>>>>>>> 5b38ca2db68537814b28a1547cbdb713040b6742
    private LocalDateTime created_time;

    public ChatMessage(ChatMessageDTO messageDTO) {
        this.room_id = messageDTO.getRoom_id();
        this.message = messageDTO.getMessage();
        this.type = messageDTO.getType();
<<<<<<< HEAD
        this.sender = messageDTO.getSender();
=======
        this.member_id = messageDTO.getSender();
>>>>>>> 5b38ca2db68537814b28a1547cbdb713040b6742
        this.created_time = LocalDateTime.now();
    }
}
