package com.example.firstproject.service;

import com.example.firstproject.entity.Board;
import com.example.firstproject.exception.ResourceNotFoundException;
import com.example.firstproject.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class BoardWriteService {
    private final BoardRepository boardRepository;

    /** 게시글 작성하기 - 매개변수 : Board board */
    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    /** 게시글 수정하기 - 매개변수 : Int no, Board board */
    public ResponseEntity<Board> updateBoard(Integer no, Board updatedBoard) {

        Board board = boardRepository.findById(no) // select no from board;
                .orElseThrow(() -> new ResourceNotFoundException("Not exist Board Data by no : ["+no+"]")); // 예외 처리
        board.setTitle(updatedBoard.getTitle()); // select title from board;
        board.setContents(updatedBoard.getContents()); // select content from board;

        Board endUpdatedBoard = boardRepository.save(board); // 값 세팅되었으면 저장하기

        return ResponseEntity.ok(endUpdatedBoard); // 성공적으로 요청이 처리되었으니 ResponseEntity를 반환
    }

    /** 게시글 삭제하기 - 매개변수 : int no */
    public ResponseEntity<Map<String, Boolean>> deleteBoard(Integer no) {
        Board board = boardRepository.findById(no)
                .orElseThrow(() -> new ResourceNotFoundException("Not exist Board Data by no : ["+no+"]"));
        boardRepository.delete(board);
        Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted Board Data by id : ["+no+"]", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}