package com.team1.rtback.dto.user;

import lombok.Getter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class SignUpRequestDto {

    // 정규식 검사
    @Size(min=4, max=10, message = "4자에서 10자 사이")
    @Pattern(regexp="^[a-z0-9]*$", message = "알파벳 소문자 a-z 숫자 0-9 가능")
    private String userId;

    @Size(min=8, max=15, message = "8자에서 15자 사이")
    @Pattern(regexp="^[a-zA-Z0-9!@#$%^&+=]*$", message = "알파벳 소문자 a-z 숫자 0-9 특수문자 가능")
    private String password;

    private String username;

    // 테스트 코드용 생성자
    public SignUpRequestDto(String userId, String password, String username) {
    }
}
