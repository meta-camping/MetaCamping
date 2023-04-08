package com.example.firstproject.dto;

import com.example.firstproject.entity.ChatRoom;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;

@Data
public class ChatRoomResponseDTO {
    private String room_name;// 채팅방 이름
    private double location_x;
    private double location_y;
    private LocalDateTime update_time;
    private HashMap<String,String> userList = new HashMap<>();

    public ChatRoomResponseDTO(ChatRoom chatRoom) {
        this.room_name = chatRoom.getRoom_name();
        this.location_x = chatRoom.getLocation_x();
        this.location_y = chatRoom.getLocation_y();
        this.update_time = chatRoom.getUpdated_time();
        //this.userList = userList;
    }
}
