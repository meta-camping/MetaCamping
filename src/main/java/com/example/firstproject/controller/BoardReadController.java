package com.example.firstproject.controller;


import com.example.firstproject.entity.Board;
import com.example.firstproject.service.BoardReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
@Log4j2
public class BoardReadController {
    private final BoardReadService boardReadService;

    // get all board
    @GetMapping("/board")
    public List<Board> getAllBoards() {

        log.info("게시판 조회");
        return boardReadService.getAllBoard();
    }
    @GetMapping("/board/{postId}")
    public ResponseEntity<Board> getBoardByNo(@PathVariable Integer postId) {
        log.info("게시판 상세보기 요청 : " + postId + "번 글");
     return boardReadService.getBoard(postId);
    }
}