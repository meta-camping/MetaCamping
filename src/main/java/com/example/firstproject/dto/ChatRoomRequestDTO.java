package com.example.firstproject.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Data
public class ChatRoomRequestDTO {

    private String room_id;  // 채팅방 아이디
    private String room_name;// 채팅방 이름
    private double location_x;
    private double location_y;
    private LocalDateTime update_time;
    private HashMap<String,String> userList = new HashMap<>();

    public ChatRoomRequestDTO(String room_name, double location_x, double location_y) {
        this.room_id = UUID.randomUUID().toString();
        this.room_name = room_name;
        this.location_x = location_x;
        this.location_y = location_y;
        this.update_time = LocalDateTime.now();
        //this.userList = userList;
    }
/*

 회원 기능 합쳐지면 이거 주석 풀고 기능 연결
    public void addUser(Member member) {
        userList.put(user.getMember_id(), Member.getMember_name());
    }

 */
}



