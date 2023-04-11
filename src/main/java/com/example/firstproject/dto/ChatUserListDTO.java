package com.example.firstproject.dto;

import com.example.firstproject.entity.ChatUserList;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ChatUserListDTO {
    private String room_id;
    private String member_id;


    public ChatUserListDTO(ChatUserList chatUserList) {
        this.room_id = getRoom_id();
        this.member_id =getMember_id();
    }
}

