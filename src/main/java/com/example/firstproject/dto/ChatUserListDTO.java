package com.example.firstproject.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ChatUserListDTO {
    private String room_id;
    private String member_id;
    private LocalDateTime join_time;

    public void UserListCheckDTO (String room_id, String member_id){
        this.room_id = room_id;
        this.member_id = member_id;
    }

}

