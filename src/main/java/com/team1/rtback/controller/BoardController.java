package com.team1.rtback.controller;

import com.team1.rtback.dto.board.BoardRequestDto;
import com.team1.rtback.dto.board.BoardResponseDto;
import com.team1.rtback.dto.global.MsgResponseDto;
import com.team1.rtback.service.BoardService;
import com.team1.rtback.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 1. 기능    : 게시글 관련 종합 컨트롤러 (게시글 CRUD)
// 2. 작성자  : 서혁수
// 추가) 1. 기능 : 게시글 좋아요,  2. 작성자 : 박영준
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 전체 글 읽기
    @GetMapping
    public List<BoardResponseDto> getAllBoard(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.getAllBoard(userDetails.getUser());
    }

    // 게시글 읽기
    @GetMapping("/{boardId}")
    public List<BoardResponseDto> getBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.getBoard(boardId, userDetails.getUser());
    }

    // 게시글 작성
    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody BoardRequestDto requestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(boardService.createBoard(requestDto, userDetails.getUser()), HttpStatus.OK);
    }

    // 게시글 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @RequestBody BoardRequestDto requestDto) {
        return new ResponseEntity<>(boardService.updateBoard(boardId, requestDto, userDetails.getUser()), HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(boardService.deleteBoard(boardId, userDetails.getUser()), HttpStatus.OK);
    }

    // 게시글 좋아요
    @PostMapping("/{boardId}/like")
    public ResponseEntity<?> boardLike(@PathVariable Long boardId,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(boardService.likeCount(boardId, userDetails.getUser()), HttpStatus.OK);
    }
}
