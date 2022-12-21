package com.team1.rtback.dto.user;

import lombok.Getter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

// 1. 기능    : 회원가입 시 입력 요소 (정규식 검사)
// 2. 작성자  : 조소영
@Getter
public class SignUpRequestDto {

    // 아이디 정규식 검사
    // @Size(min=4, max=10, message = "4자에서 10자 사이")
    @Pattern(regexp="^(?=.*[a-z])(?=.*\\d)[a-z\\d]{4,10}$",
            message = "최소 4 자 및 최대 10 자, 최소 하나의 소문자 및 하나의 숫자")
    private String userId;

    // 비밀번호 정규식 검사
    // @Size(min=8, max=15, message = "8자에서 15자 사이")
    @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,15}",
            message = "최소 8 자 및 최대 15 자, 대문자 하나 이상, 소문자 하나, 숫자 하나 및 특수 문자 하나 이상")
    private String password;

    // 유저 닉네임
    private String username;
}
