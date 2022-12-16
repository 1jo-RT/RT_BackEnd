package com.team1.rtback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 1. 기능 : Spring Security 설정
// 2. 작성자 : 조소영
// 3. 생성날자 : 2022-12-16
@Entity
@Table(name="USERS")
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    public User(String userId, String username, String password){
        this.userId = userId;
        this.username = username;
        this.password = password;
    }
}
