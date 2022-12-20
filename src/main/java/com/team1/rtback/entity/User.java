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

    @Column(nullable = false, unique = true)            // 카카오ID로 form 가입하는 걸 막기위해 UUID로 설정
    private String userId;                              // 유저 아이디

    @Column
    private Long kakaoId;

    @Column(nullable = false, unique = true)
    private String username;                            // 유저 닉네임

    @Column(nullable = false)
    private String password;                            // 유저 비밀번호

    @Column
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    // 유저 생성 메서드
    public User(String userId, String username, String password,  UserRoleEnum role){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String userId, String username, Long kakaoId, String email, String password,  UserRoleEnum role){
        this.userId = userId;
        this.kakaoId = kakaoId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // 카카오 아이디 업데이트
    public User kakaoIdUpdate(Long kakaoId){
        this.kakaoId = kakaoId;
        return this;
    }
}

