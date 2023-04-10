package com.example.firstproject.service;

import com.example.firstproject.dto.ChatRoomRequestDTO;
import com.example.firstproject.dto.ChatRoomResponseDTO;
import com.example.firstproject.entity.ChatRoom;
import com.example.firstproject.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;


    //채팅방 생성
    public ChatRoom createRoom(ChatRoomRequestDTO requestDto) {
        ChatRoom chatRoom = new ChatRoom(requestDto);
        return chatRoomRepository.save(chatRoom);
    }

    //채팅방 리스트 불러오기
    public List<ChatRoom> findAllRoom() {
        //채팅방 최근 생성 순으로 반환
        List<ChatRoom> result = new ArrayList<>(chatRoomRepository.findAll());
        Collections.reverse(result);
        return result;
    }
    //채팅방 하나 불러오기
    public ChatRoomResponseDTO findRoomById(String room_id) {
        ChatRoom chatRoom = chatRoomRepository.findById(room_id) .orElse(null);
        return new ChatRoomResponseDTO(chatRoom);
        /*

        회원 기능 병합 되면 주석 풀고 연결

        HttpSession session = request.getSession();
        String member_id = (String) session.getAttribute("member_id");
        // 유저를 userList에 추가하여 DB에 업데이트
        chatRoom.getUserList().put(member_id, member_name);

        // 채팅방 참여 인원 표시 - userList의 count 수만 가져오기
        chatRoom.setNumberOfUsers(chatRoom.getUserList().size());


         //채팅방 내부 userList 조회

         */
    }

}
