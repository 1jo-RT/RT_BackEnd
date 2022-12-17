package com.team1.rtback.entity;

import com.team1.rtback.dto.comment.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor

public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        //댓글 번호

    @Column(nullable = false)
    private String username;        //닉네임

    @ManyToOne
    @JoinColumn(name = "userIdx", nullable = false)
    private User user;          //작성자 id

    @ManyToOne
    @JoinColumn(name = "boardIdx", nullable = false)
    private Board board;        //게시글 id

    @Column(nullable = false)
    private String comment;         //댓글 내용

    public Comment(CommentRequestDto commentRequestDto, Board board, User user) {
        this.username = user.getUsername();
        this.user = user;
        this.board = board;
        this.comment = commentRequestDto.getComment();
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
    }
}
