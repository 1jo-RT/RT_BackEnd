package com.team1.rtback.service;

import com.team1.rtback.dto.comment.CommentRequestDto;
import com.team1.rtback.dto.comment.CommentResponseDto;
import com.team1.rtback.entity.Board;
import com.team1.rtback.entity.Comment;
import com.team1.rtback.entity.User;
import com.team1.rtback.exception.CustomException;
import com.team1.rtback.repository.BoardRepository;
import com.team1.rtback.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.team1.rtback.exception.ErrorCode.AUTHORIZATION;
import static com.team1.rtback.exception.ErrorCode.NOT_FOUND_BOARD;

// 1. 기능    : 댓글 서비스
// 2. 작성자  : 박영준
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 댓글 작성
    @Transactional
    public void createComment(Long id, CommentRequestDto commentRequestDto, User user) {

        // 1. 요청한 게시글 존재여부 확인
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        // 2. 게시글에 댓글 작성
        Comment comment = commentRepository.save(new Comment(commentRequestDto, board, user));

        new CommentResponseDto(comment);
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long boardId, Long cmtId, CommentRequestDto commentRequestDto, User user) {

        // 1. 요청한 게시글 존재여부 확인
        Board board = boardRepository.findById(boardId).orElseThrow (
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        // 2. 댓글 수정 권한 검증
        Comment comment;
        comment = commentRepository.findByIdAndUserId(cmtId, user.getId()).orElseThrow(
                () -> new CustomException(AUTHORIZATION)
        );

        // 3. 해당 댓글 수정
        comment.update(commentRequestDto);

        new CommentResponseDto(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long boardId, Long cmtId, User user) {

        // 1. 요청한 게시글 존재여부 확인
        Board board = boardRepository.findById(boardId).orElseThrow (
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        // 2. 댓글 삭제 권한 검증
        Comment comment;
        comment = commentRepository.findByIdAndUserId(cmtId, user.getId()).orElseThrow(
                () -> new CustomException(AUTHORIZATION)
        );

        // 3. 해당 댓글 삭제
        commentRepository.deleteById(cmtId);

        new CommentResponseDto(comment);
    }
}
