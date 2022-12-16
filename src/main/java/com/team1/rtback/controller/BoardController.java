package com.team1.rtback.controller;

import com.team1.rtback.dto.board.BoardRequestDto;
import com.team1.rtback.dto.board.BoardResponseDto;
import com.team1.rtback.dto.global.GlobalDto;
import com.team1.rtback.entity.User;
import com.team1.rtback.repository.BoardRepository;
import com.team1.rtback.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardRepository boardRepository;
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
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto) {
        return boardService.createBoard(requestDto);
    }

    @PutMapping("/boards/{boardId}")
    public BoardResponseDto updateBoard(@PathVariable Long boardId,
                                        @RequestBody BoardRequestDto requestDto) {
        return boardService.updateBoard(boardId, requestDto);
    }

    @DeleteMapping("/boards/{boardId}")
    public GlobalDto deleteBoard(@PathVariable Long boardId) {
        return boardService.deleteBoard(boardId);
    }
}
