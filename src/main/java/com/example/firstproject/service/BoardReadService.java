package com.example.firstproject.service;


import com.example.firstproject.entity.Board;
import com.example.firstproject.exception.ResourceNotFoundException;
import com.example.firstproject.repository.BoardRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardReadService {
    private final BoardRepository boardRepository;

    /** 전체 글 조회 (글 목록) */
    public List<Board> getAllBoard() {
        return boardRepository.findAll();
    }

    /** 글 상세 보기 (글 클릭해서 단일 항목 보기) */
    public ResponseEntity<Board> getBoard(Integer postId) {
        Board board = boardRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Not exist Board Data by postId : ["+postId+"]"));
        return ResponseEntity.ok(board);
    }
}