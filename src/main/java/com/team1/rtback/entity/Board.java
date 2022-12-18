package com.team1.rtback.entity;

import com.team1.rtback.dto.board.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// 1. 기능    : 게시글 구성요소
// 2. 작성자  : 서혁수
// 추가) 1. 기능 : 게시글 좋아요,  2. 작성자 : 박영준
@Entity
@Getter
@NoArgsConstructor
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardIdx")
    private Long id;                                        // 게시글 번호

    @Column(nullable = false)
    private String title;                                   // 게시글 제목

    @ManyToOne
    @JoinColumn(name = "userIdx", nullable = false)
    private User user;                                      // 게시글 작성자 정보

    @Column(nullable = false)
    private String username;                                // 게시글 작성자 명

    @Column(nullable = false)
    private String content;                                 // 게시글 내용

    @Column
    private String imgUrl;                                  // 게시글 이미지 파일 정보

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<BoardLike> boardLikeList = new ArrayList<>();          // 게시글 좋아요 (게시글 자신이 받을 좋아요의 개수를 알고 있어야함. 그래서 양방향 관계)

    // 게시글 작성 메서드
    public Board(BoardRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = user.getUsername();
        this.imgUrl = requestDto.getImgUrl();
        this.user = user;
    }

    // 게시글 수정 메서드
    public void update(BoardRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.imgUrl = requestDto.getImgUrl();
        this.user = user;
    }
}