package com.team1.rtback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

// 1. 기능    : 게시글 좋아요 구성요소
// 2. 작성자  : 박영준
@Getter
@Entity
@NoArgsConstructor
public class BoardLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                                        // 게시글 좋아요 번호

    @ManyToOne
    @JoinColumn(name = "userIdx", nullable = false)
    private User user;                                      // 유저 정보

    @ManyToOne
    @JoinColumn(name = "boardIdx", nullable = false)
    private Board board;                                    // 게시글 고유번호

    public BoardLike(Board board, User user) {
        this.board = board;
        this.user = user;
    }
}
