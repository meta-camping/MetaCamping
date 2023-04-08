package com.example.firstproject.repository;

import com.example.firstproject.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    @Query("SELECT cr.user_list FROM ChatRoom cr WHERE cr.room_id = :room_id")
    List<ChatRoom> findUserListByChatRoomId(@Param("room_id") String room_id);

}
