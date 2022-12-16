package com.team1.rtback.dto.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 1. 기능 : 성공 메세지 커스텀
// 2. 작성자 : 조소영
// 3. 생성날자 : 2022-12-16
@Getter
@AllArgsConstructor
public enum SuccessCode {

    JOIN_OK(HttpStatus.OK, "가입 성공했습니다"),
    LOGIN_OK(HttpStatus.OK, "로그인 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String msg;
}
