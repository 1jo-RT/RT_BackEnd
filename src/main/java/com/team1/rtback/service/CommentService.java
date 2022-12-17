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

@Service
@RequiredArgsConstructor

public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    //댓글 작성
    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto commentRequestDto, User user) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("없는 글임")
        );

        Comment comment = commentRepository.save(new Comment(commentRequestDto, board, user));

        return new CommentResponseDto(comment);
    }

    //댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long boardId, Long cmtId, CommentRequestDto commentRequestDto, User user) {
        //DB 에 게시글 저장 확인
        Board board = boardRepository.findById(boardId).orElseThrow (
                () -> new IllegalArgumentException("없는 글임")
        );

        Comment comment;

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

        comment = commentRepository.findByIdAndUserId(cmtId, user.getId()).orElseThrow(
                () -> new IllegalArgumentException("계정 불일치")
        );

        comment.update(commentRequestDto);

        return new CommentResponseDto(comment);
    }

    //댓글 삭제
    @Transactional
    public CommentResponseDto deleteComment(Long boardId, Long cmtId, User user) {
        //DB 에 게시글 저장 확인
        Board board = boardRepository.findById(boardId).orElseThrow (
                () -> new IllegalArgumentException("없는 글임")
        );

        Comment comment;

//        if (user.getRole().equals(UserRoleEnum.ADMIN)) {
//            comment = commentRepository.findById(cmtId).orElseThrow(
//                    () -> new IllegalArgumentException("없는 댓글")
//            );
//        } else {
//            comment = commentRepository.findByIdAndUserId(cmtId, user.getId()).orElseThrow(
//                    () -> new IllegalArgumentException("계정 불일치")
//            );
//        }

        comment = commentRepository.findByIdAndUserId(cmtId, user.getId()).orElseThrow(
                () -> new IllegalArgumentException("계정 불일치")
        );

        //해당 댓글 삭제
        commentRepository.deleteById(cmtId);

        return new CommentResponseDto(comment);
    }
}
