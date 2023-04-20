package com.example.firstproject.repository;

import com.example.firstproject.dto.ChatUserListDTO;
import com.example.firstproject.entity.ChatUserList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatUserListRepository extends JpaRepository<ChatUserList,String> {

    @Query("SELECT cul FROM chat_user_list cul WHERE cul.room_id = :room_id AND cul.member_id = :member_id")
    ChatUserList findUserList(@Param("room_id") String room_id, @Param("member_id") String member_id);

    public default boolean isUserInRoom(ChatUserListDTO userListCheckDTO) {
        ChatUserList userList = findUserList(userListCheckDTO.getRoom_id(), userListCheckDTO.getMember_id());
        return userList != null;
    }
}

