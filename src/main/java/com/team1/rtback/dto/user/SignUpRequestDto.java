package com.team1.rtback.dto.user;

import lombok.Getter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

// 1. 기능    : 회원가입 시 입력 요소 (정규식 검사)
// 2. 작성자  : 조소영
@Getter
public class SignUpRequestDto {

    // 아이디 정규식 검사
    @Size(min=4, max=10, message = "4자에서 10자 사이")
    @Pattern(regexp="^[a-z0-9]*$", message = "알파벳 소문자 a-z 숫자 0-9 가능")
    private String userId;

    // 비밀번호 정규식 검사
    @Size(min=8, max=15, message = "8자에서 15자 사이")
    @Pattern(regexp="^[a-zA-Z0-9!@#$%^&+=]*$", message = "알파벳 소문자 a-z 숫자 0-9 특수문자 가능")
    private String password;

    // 유저 닉네임
    private String username;
}
