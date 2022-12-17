package com.team1.rtback.entity;

import com.team1.rtback.dto.comment.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

// 1. 기능    : 댓글 구성요소
// 2. 작성자  : 박영준
@Entity
@Getter
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                                        // 댓글 번호

    @Column(nullable = false)
    private String username;                                // 유저 닉네임

    @ManyToOne
    @JoinColumn(name = "userIdx", nullable = false)
    private User user;                                      // 유저 정보

    @ManyToOne
    @JoinColumn(name = "boardIdx", nullable = false)
    private Board board;                                    // 게시글 고유번호

    @Column(nullable = false)
    private String comment;                                 // 댓글 내용

    // 댓글 작성 메서드
    public Comment(CommentRequestDto commentRequestDto, Board board, User user) {
        this.username = user.getUsername();
        this.user = user;
        this.board = board;
        this.comment = commentRequestDto.getComment();
    }

    // 댓글 수정 메서드
    public void update(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
    }
}