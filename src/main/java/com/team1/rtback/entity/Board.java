package com.team1.rtback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardidx")
    private Long Id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "userIdx", nullable = false)
    private User user;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;

    @Column
    private String imgUrl;
}