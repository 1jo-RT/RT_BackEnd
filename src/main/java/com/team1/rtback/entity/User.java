package com.team1.rtback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 1. 기능 : Spring Security 설정
// 2. 작성자 : 조소영
@Entity
@Table(name="USERS")
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;                                    // 유저 고유번호

    @Column(nullable = false)
    private String userId;                              // 유저 아이디

    @Column(nullable = false)
    private String username;                            // 유저 닉네임

    @Column(nullable = false)
    private String password;                            // 유저 비밀번호

    // 유저 생성 메서드
    public User(String userId, String username, String password){
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
}

