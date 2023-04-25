package com.example.firstproject.entity;

import com.example.firstproject.dto.ChatMessageDTO;
import com.example.firstproject.dto.ChatRoomRequestDTO;
import com.example.firstproject.dto.ChatRoomResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="chat_user_list")
public class ChatUserList {

    @Id
    @Column(name = "member_id")
    private String memberId; //유니크로 중복을 배제

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "join_time")
    private LocalDateTime joinTime;


}
