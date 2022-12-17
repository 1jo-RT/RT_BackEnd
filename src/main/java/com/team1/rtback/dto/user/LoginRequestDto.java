package com.team1.rtback.dto.user;

import lombok.Getter;

// 1. 기능    : 로그인 시 입력 요소
// 2. 작성자  : 조소영
@Getter
public class LoginRequestDto {
    private String userId;      // 유저 아이디
    private String password;    // 유저 비밀번호
}
