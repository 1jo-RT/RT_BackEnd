package com.team1.rtback.service;

import com.team1.rtback.dto.board.BoardRequestDto;
import com.team1.rtback.dto.board.BoardResponseDto;
import com.team1.rtback.dto.global.GlobalDto;
import com.team1.rtback.entity.Board;
import com.team1.rtback.entity.Comment;
import com.team1.rtback.entity.User;
import com.team1.rtback.repository.BoardRepository;
import com.team1.rtback.repository.CommentRepository;
import com.team1.rtback.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
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
    private final CommentRepository commentRepository;

    // 전체 글 읽기
    public List<BoardResponseDto> getAllBoard() {

        List<Board> boardList = boardRepository.findAll();
        ArrayList<BoardResponseDto> result = new ArrayList<>();

        for (Board board : boardList) {
            result.add(new BoardResponseDto(board));
        }

        return result;
    }

    // 게시글 읽기
    public BoardResponseDto getBoard(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("없는 글임")
        );

        return new BoardResponseDto(board);
    }

    // 게시글 작성
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, User user) {
        Board board = new Board(requestDto, user);
        boardRepository.save(board);
        return new BoardResponseDto(board, user.getId());
    }

    // 게시글 수정
    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto, User user) {

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("없는 글임")
        );

        if (user.getId() == board.getUser().getId()) {
            board.update(requestDto, user);
        } else {
            throw new IllegalArgumentException("계정 불일치");
        }

        return new BoardResponseDto(board, user.getId());
    }

    // 게시글 삭제
    public GlobalDto deleteBoard(Long boardId, User user) {

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("없는 글임")
        );

        if (user.getId() != board.getUser().getId()) {
            throw new IllegalArgumentException("님 글 아님");
        }

        boardRepository.delete(board);

        return new GlobalDto(200, "삭제 완료");
    }
}
