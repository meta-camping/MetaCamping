package com.example.firstproject.repository;

import com.example.firstproject.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository  extends JpaRepository<ChatMessage, String> {
}
