package com.example.firstproject.entity;

import com.example.firstproject.dto.ChatRoomRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@Entity
public class ChatRoom {
    @Id
    @Column(name = "room_id")
    private String room_id;
    @Column(name = "room_name")
    private String room_name; //캠핑장 이름이어야 함
    @Column(name = "location_x")
    private double location_x;
    @Column(name = "location_y")
    private double location_y;
    @Column(name = "user_list")
    private String user_list;
    @CreatedDate
    @Column(name = "created_time")
<<<<<<< HEAD
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_time;
    @LastModifiedDate
    @Column(name = "updated_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
=======
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_time;
    @LastModifiedDate
    @Column(name = "updated_time")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
>>>>>>> 5b38ca2db68537814b28a1547cbdb713040b6742
    private LocalDateTime updated_time;
/*
    @Transient //컬럼으로 구성하여 관리할 필요가 없는 데이터에 다는 어노테이션
    //일단 해쉬맵으로 해놨지만 member entity 생기면 그것과 연결하고 chatroomrepository도 수정
    private HashMap<String, String> userList;
 */

    public ChatRoom(ChatRoomRequestDTO requestDto) {
        this.room_id = UUID.randomUUID().toString();
        this.room_name = requestDto.getRoom_name();
        this.location_x = requestDto.getLocation_x();
        this.location_y = requestDto.getLocation_y();
        this.created_time = LocalDateTime.now();
        this.updated_time = LocalDateTime.now();
    }

    //채팅방 최근 활성화 시간
    public void update(LocalDateTime update_time) {
        if (update_time.isAfter(this.updated_time)) {
            this.updated_time = update_time;
        }
    }

    /*
    public static ChatRoom create(String room_name) {
        ChatRoom room = new ChatRoom();
        room.room_id = UUID.randomUUID().toString();
        room.room_name = "테스트 캠핑장"; //캠핑장 테이블의 이름을 가져올 것
        room.location_x = 127.0; //캠핑장 좌표 조인
        room.location_y = 127.0; //캠핑장 좌표 조인
        room.userlist = ChatRoomDTO.getUserlist();
        return room;
    }
     */
}