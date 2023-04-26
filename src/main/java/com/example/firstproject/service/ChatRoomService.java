package com.example.firstproject.service;

import com.example.firstproject.dto.*;
import com.example.firstproject.entity.ChatRoom;
import com.example.firstproject.entity.ChatUserList;
import com.example.firstproject.repository.ChatRoomRepository;
import com.example.firstproject.repository.ChatUserListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatUserListRepository chatUserListRepository;

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
    public ChatRoomResponseDTO findRoomByRoomId(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId) .orElse(null);
        return new ChatRoomResponseDTO(chatRoom);
    }
    //채팅방 유저리스트 조회
    public List<ChatUserListResponseDTO> findUserListRoomByRoomId(String roomId) {
        List<ChatUserListResponseDTO> userList = chatUserListRepository.findAllByRoomId(roomId);
        return userList;
    }

    public String DeleteUserList(String roomId, String memberId) {
        ChatUserList userList = chatUserListRepository.findUserList(roomId,memberId);
        chatUserListRepository.delete(userList);
        return "삭제 완료";
    }

    public String findRoomByRoomName(String roomName) {
        String result = chatRoomRepository.existsByRoomName(roomName);
        return result;
    }



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


    //채팅방 유저 리스트에 참가자 추가
    public ChatUserList insertUserList(ChatMessageDTO chatMessage) {
            ChatUserList chatUserList = new ChatUserList(
                    chatMessage.getSender(), chatMessage.getRoomId(), LocalDateTime.now());
            return chatUserListRepository.save(chatUserList);
    }

    //유저 기참여여부 확인
    public String isUserInRoom(ChatUserListDTO userListCheckDTO) {
        ChatUserList userList = chatUserListRepository.findUserList(userListCheckDTO.getRoomId(), userListCheckDTO.getMemberId());
        if (userList != null) {
            return "InUser";
        } else {
            ChatUserList memberCheck = chatUserListRepository.findOtherUserList(userListCheckDTO.getRoomId(), userListCheckDTO.getMemberId());
            if (memberCheck != null) {
                return "OtherInUser";
            }
            return "OK";
        }
        // 일치하는 조건이 없는 경우

    }

}
