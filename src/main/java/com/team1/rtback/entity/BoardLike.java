package com.team1.rtback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "boardIdx", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "userIdx", nullable = false)
    private User user;

    public BoardLike(Board board, User user) {
        this.board = board;
        this.user = user;
    }
}
