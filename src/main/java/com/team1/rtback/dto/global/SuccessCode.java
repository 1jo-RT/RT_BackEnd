package com.team1.rtback.dto.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    JOIN_OK(HttpStatus.OK, "가입 성공했습니다");

    private final HttpStatus httpStatus;
    private final String msg;
}
