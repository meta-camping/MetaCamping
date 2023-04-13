package com.example.firstproject.controller;

import com.example.firstproject.dto.ChatRoomRequestDTO;
import com.example.firstproject.dto.ChatRoomResponseDTO;
import com.example.firstproject.dto.ChatUserListDTO;
import com.example.firstproject.entity.ChatRoom;
import com.example.firstproject.repository.ChatRoomRepository;
import com.example.firstproject.repository.ChatUserListRepository;
import com.example.firstproject.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@CrossOrigin("*")
public class ChatRoomContoller {
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserListRepository chatUserListRepository;

    // 채팅 리스트 화면
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<ChatRoom>> getRoomList() {
        List<ChatRoom> chatRooms = chatRoomService.findAllRoom();
        return new ResponseEntity<>(chatRooms, HttpStatus.OK);
    }

    // 채팅방 생성
    @PostMapping("/create")
    public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoomRequestDTO chatRoomRequestDTO) {
        chatRoomService.createRoom(chatRoomRequestDTO);
        return new ResponseEntity<ChatRoom>(HttpStatus.OK);
    }

    // 특정 채팅방 조회
    // 채팅방 정보를 가져오는 메서드
    @GetMapping("/room/{room_id}")
    @ResponseBody
    public ResponseEntity<ChatRoomResponseDTO> getRoomInfo(@PathVariable String room_id) {
        // roomId를 이용하여 채팅방 정보를 조회하고 ResponseDTO를 생성하여 반환합니다.
        ChatRoomResponseDTO chatRoomResponseDTO = chatRoomService.findRoomById(room_id);
        if (chatRoomResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chatRoomResponseDTO);
    }

    //채팅방 기참여 여부 조회
    @PostMapping("/room/user-check")
    public ResponseEntity<Integer> userInRoomCheck(@RequestBody ChatUserListDTO user) {
        Boolean result = chatRoomService.isUserInRoom(user);
                if (result == false){
                    return ResponseEntity.ok(1);
                }
                else {
                        return ResponseEntity.ok(2);
                    }
                }
    }
