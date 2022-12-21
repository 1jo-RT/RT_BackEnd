package com.team1.rtback.dto.board;

import com.team1.rtback.dto.comment.CommentResponseDto;
import com.team1.rtback.entity.Board;
import com.team1.rtback.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 1. 기능    : 게시글 반환용 DTO
// 2. 작성자  : 서혁수
// 추가) 1. 기능 : 게시글 좋아요,  2. 작성자 : 박영준
@Getter
@NoArgsConstructor
public class BoardResponseDto {
    private LocalDateTime createdAt;        // 작성날짜
    private LocalDateTime modifiedAt;       // 수정날짜
    private Long id;                        // 게시글 고유번호
    private String title;                   // 게시글 제목
    private Long userIdx;                   // 게시글 작성자 고유번호
    private String username;                // 게시글 작성자 명
    private String content;                 // 게시글 내용
    private Long boardLikeCount;             // 게시글 좋아요
    private boolean boardLikeCheck;         // 게시글 좋아요 여부 (true/false)
    private List<CommentResponseDto> commentList = new ArrayList<>();


    // 게시글 작성용 메서드
    public BoardResponseDto(Board board, Long userId) {
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.id = board.getId();
        this.title = board.getTitle();
        this.userIdx = userId;
        this.username = board.getUsername();
        this.content = board.getContent();
    }

    // 게시글 조회용 메서드
    public BoardResponseDto(Board board, List<CommentResponseDto> commentList, boolean boardLikeCheck) {
        this.createdAt = board.getCreatedAt();
        this.modifiedAt = board.getModifiedAt();
        this.id = board.getId();
        this.title = board.getTitle();
        this.userIdx = board.getUser().getId();
        this.username = board.getUsername();
        this.content = board.getContent();
        this.commentList = commentList;
        this.boardLikeCount = (long) board.getBoardLikeList().size();
        this.boardLikeCheck = boardLikeCheck;
    }

    public BoardResponseDto(BoardResponseDto boardResponseDto, List<CommentResponseDto> commentList, boolean boardLikeCheck) {
        this.createdAt = boardResponseDto.getCreatedAt();
        this.modifiedAt = boardResponseDto.getModifiedAt();
        this.id = boardResponseDto.getId();
        this.title = boardResponseDto.getTitle();
        this.userIdx = boardResponseDto.getUserIdx();
        this.username = boardResponseDto.getUsername();
        this.content = boardResponseDto.getContent();
        this.commentList = commentList;
        this.boardLikeCount = boardResponseDto.getBoardLikeCount();
        this.boardLikeCheck = boardLikeCheck;
    }
}