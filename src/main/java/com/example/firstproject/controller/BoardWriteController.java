package com.example.firstproject.controller;


import com.example.firstproject.entity.Board;
import com.example.firstproject.service.BoardWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
@Log4j2
public class BoardWriteController {
    private final BoardWriteService boardWriteService;
    @PostMapping("/board")
    public ResponseEntity<Board> createBoard(@RequestBody Board board) {
        try {
            log.info("게시판 글 쓰기 요청 : " + board.getPostId() + "번 글");
            Board createBoard = boardWriteService.createBoard(board);
            return new ResponseEntity<>(createBoard, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("글 쓰기 중 오류 발생", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/board/{postId}")
    public ResponseEntity<Board> updateBoardByNo(
            @PathVariable Integer postId, @RequestBody Board board){
        log.info("게시판 수정 요청 : " + postId + "번 글");
        return boardWriteService.updateBoard(postId, board);
    }
    @DeleteMapping("/board/{postId}")
    public ResponseEntity<Map<String, Boolean>> deleteBoardByNo(
            @PathVariable Integer postId) {
        log.info("게시글 삭제 : " + postId + "번 글");
        return boardWriteService.deleteBoard(postId);
    }
}