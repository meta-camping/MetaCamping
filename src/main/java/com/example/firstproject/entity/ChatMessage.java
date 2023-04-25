package com.example.firstproject.entity;

import com.example.firstproject.dto.ChatMessageDTO;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@RequiredArgsConstructor
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long messageId;
    @Column(name = "room_id")
    private String roomId;
    @Column(name = "type")
    private ChatMessageDTO.MessageType type;
    //채팅방 ID
    //보내는 사람
    @Column(name = "member_id")
    private String memberId;
    //내용
    @Column(name = "message")
    private String message;
    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    public ChatMessage(ChatMessageDTO messageDTO) {
        this.roomId = messageDTO.getRoomId();
        this.message = messageDTO.getMessage();
        this.type = messageDTO.getType();
        this.memberId = messageDTO.getSender();
        this.createdTime = LocalDateTime.now();
    }
}