package com.team1.rtback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
public class BoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Long boardIdx;

    @Column
    private Long userIdx;

    @Column(nullable = false)
    private String imgUrl;

    public BoardImage(Long boardIdx, Long userIdx, String imgUrl) {
        this.boardIdx = boardIdx;
        this.userIdx = userIdx;
        this.imgUrl = imgUrl;
    }

}
