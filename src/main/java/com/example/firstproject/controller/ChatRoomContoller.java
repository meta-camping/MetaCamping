package com.example.firstproject.controller;

import com.example.firstproject.dto.ChatRoomRequestDTO;
import com.example.firstproject.dto.ChatRoomResponseDTO;
import com.example.firstproject.entity.ChatRoom;
import com.example.firstproject.repository.ChatRoomRepository;
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

    // 채팅 리스트 화면
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<ChatRoom>> getRoomList() {
        List<ChatRoom> chatRooms = chatRoomService.findAllRoom();
        return new ResponseEntity<>(chatRooms, HttpStatus.OK);
    }

    // 채팅방 생성
    @PostMapping("/create")
    public ChatRoom createRoom(@RequestBody ChatRoomRequestDTO chatRoomRequestDTO) {
        return chatRoomService.createRoom(chatRoomRequestDTO);
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


    }
