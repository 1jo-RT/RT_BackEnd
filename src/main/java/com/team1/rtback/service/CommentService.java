package com.team1.rtback.service;

import com.team1.rtback.dto.comment.CommentRequestDto;
import com.team1.rtback.dto.comment.CommentResponseDto;
import com.team1.rtback.entity.Board;
import com.team1.rtback.entity.Comment;
import com.team1.rtback.entity.User;
import com.team1.rtback.repository.BoardRepository;
import com.team1.rtback.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 1. 기능    : 댓글 서비스
// 2. 작성자  : 박영준
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 댓글 작성
    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto commentRequestDto, User user) {

        // 1. 요청한 게시글 존재여부 확인
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("없는 글임")
        );

        // 2. 게시글에 댓글 작성
        Comment comment = commentRepository.save(new Comment(commentRequestDto, board, user));

        return new CommentResponseDto(comment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long boardId, Long cmtId, CommentRequestDto commentRequestDto, User user) {

        // 1. 요청한 게시글 존재여부 확인
        Board board = boardRepository.findById(boardId).orElseThrow (
                () -> new IllegalArgumentException("없는 글임")
        );

//        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
//            comment = commentRepository.findById(cmtId).orElseThrow(
//                    () -> new IllegalArgumentException("없는 댓글")
//            );
//
//        } else {
//            comment = commentRepository.findByIdAndUserId(cmtId, user.getId()).orElseThrow(
//                    () -> new IllegalArgumentException("계정 불일치")
//            );
//        }
        // 2. 댓글 수정 권한 검증
        Comment comment;
        comment = commentRepository.findByIdAndUserId(cmtId, user.getId()).orElseThrow(
                () -> new IllegalArgumentException("계정 불일치")
        );

        // 3. 해당 댓글 수정
        comment.update(commentRequestDto);

        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    @Transactional
    public CommentResponseDto deleteComment(Long boardId, Long cmtId, User user) {

        // 1. 요청한 게시글 존재여부 확인
        Board board = boardRepository.findById(boardId).orElseThrow (
                () -> new IllegalArgumentException("없는 글임")
        );

//        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
//            comment = commentRepository.findById(cmtId).orElseThrow(
//                    () -> new IllegalArgumentException("없는 댓글")
//            );
//        } else {
//            comment = commentRepository.findByIdAndUserId(cmtId, user.getId()).orElseThrow(
//                    () -> new IllegalArgumentException("계정 불일치")
//            );
//        }
        // 2. 댓글 삭제 권한 검증
        Comment comment;
        comment = commentRepository.findByIdAndUserId(cmtId, user.getId()).orElseThrow(
                () -> new IllegalArgumentException("계정 불일치")
        );

        // 3. 해당 댓글 삭제
        commentRepository.deleteById(cmtId);

        return new CommentResponseDto(comment);
    }
}
