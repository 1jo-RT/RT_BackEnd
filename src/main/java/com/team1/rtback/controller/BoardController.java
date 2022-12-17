package com.team1.rtback.controller;

import com.team1.rtback.dto.board.BoardRequestDto;
import com.team1.rtback.dto.board.BoardResponseDto;
import com.team1.rtback.service.BoardService;
import com.team1.rtback.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boards")
    public List<BoardResponseDto> getAllBoard() {
        return boardService.getAllBoard();
    }

    @GetMapping("/boards/{boardId}")
    public BoardResponseDto getBoard(@PathVariable Long boardId) {
        return boardService.getBoard(boardId);
    }

    @PostMapping("/boards")
    public ResponseEntity<?> createBoard(@RequestBody BoardRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(boardService.createBoard(requestDto, userDetails.getUser()), HttpStatus.OK);
    }

    @PutMapping("/boards/{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestBody BoardRequestDto requestDto) {
        return new ResponseEntity<>(boardService.updateBoard(boardId, requestDto, userDetails.getUser()), HttpStatus.OK);
    }

    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(boardService.deleteBoard(boardId, userDetails.getUser()), HttpStatus.OK);
    }
}
