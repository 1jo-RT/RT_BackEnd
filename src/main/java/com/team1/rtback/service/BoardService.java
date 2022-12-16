package com.team1.rtback.service;

import com.team1.rtback.dto.board.BoardRequestDto;
import com.team1.rtback.dto.board.BoardResponseDto;
import com.team1.rtback.dto.global.GlobalDto;
import com.team1.rtback.entity.Board;
import com.team1.rtback.entity.User;
import com.team1.rtback.repository.BoardRepository;
import com.team1.rtback.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;


@Builder
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<BoardResponseDto> getAllBoard() {

        List<Board> boardList = boardRepository.findAll();
        ArrayList<BoardResponseDto> result = new ArrayList<>();

        for (Board board : boardList) {
            result.add(new BoardResponseDto(board));
        }

        return result;
    }


    public BoardResponseDto getBoard(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("없는 글임")
        );

        return new BoardResponseDto(board);
    }


    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto) {

        User user = userRepository.findById(requestDto.getUserIdx()).orElseThrow(
                () -> new IllegalArgumentException("없는 계정")
        );

        Board board = new Board(requestDto, user);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }


    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto) {

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("없는 글임")
        );

        if (requestDto.getUserIdx() == board.getUserIdx()) {
            board.update(requestDto);
        } else {
            throw new  IllegalArgumentException("계정 불일치");
        }
        return new BoardResponseDto(board);
    }


    public GlobalDto deleteBoard(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("없는 글임")
        );

        boardRepository.delete(board);

        return new GlobalDto(200, "삭제 완료");
    }
}
