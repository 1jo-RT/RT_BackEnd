package com.team1.rtback.controller;

import com.team1.rtback.dto.board.BoardRequestDto;
import com.team1.rtback.service.BoardService;
import com.team1.rtback.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

// 1. 기능    : 게시글 관련 종합 컨트롤러 (게시글 CRUD)
// 2. 작성자  : 서혁수
// 추가) 1. 기능 : 게시글 좋아요,  2. 작성자 : 박영준
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 전체 글 조회
    @GetMapping
    public ResponseEntity<?> getAllBoard(HttpServletRequest request) {
        return new ResponseEntity<>(boardService.getAllBoard(request), (HttpStatus.OK));
    }

    // 단건 글 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId,
                                      HttpServletRequest request) {
        return new ResponseEntity<>(boardService.getBoard(boardId, request), HttpStatus.OK);
    }

    // 새 글 작성
    @PostMapping(value = "/newboard", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createBoard(@RequestPart BoardRequestDto requestDto,
                                         @RequestPart("image") MultipartFile multipartFile,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return new ResponseEntity<>(boardService.createBoard(requestDto, multipartFile, userDetails.getUser()), HttpStatus.OK);
    }

    // 글 수정
    @PutMapping("/newboard/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId,
                                         @RequestPart BoardRequestDto requestDto,
                                         @RequestPart("image") MultipartFile multipartFile,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return new ResponseEntity<>(boardService.updateBoard(boardId, requestDto, multipartFile, userDetails.getUser()), HttpStatus.OK);
    }

    // 게시글 삭제
    @DeleteMapping("/delboard/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deleteBoard(boardId, userDetails.getUser());
    }

    // 게시글 좋아요
    @PostMapping("/{boardId}/like")
    public ResponseEntity<?> boardLike(@PathVariable Long boardId,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.createBoardLike(boardId, userDetails.getUser());
    }

}
