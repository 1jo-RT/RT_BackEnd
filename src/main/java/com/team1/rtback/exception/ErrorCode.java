package com.team1.rtback.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 1. 기능    : 예외처리
// 2. 작성자  : 박영준
@Getter
@AllArgsConstructor
public enum ErrorCode {
    //게시글, 댓글 관련
    NOT_FOUND_BOARD(HttpStatus.BAD_REQUEST, "게시글을 찾을 수 없습니다."),
    AUTHORIZATION(HttpStatus.BAD_REQUEST, "작성자만 수정/삭제할 수 있습니다."),

    // user 관련
    DUPLICATED_USERID(HttpStatus.BAD_REQUEST, "존재하는 아이디 입니다"),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "패스워드가 일치하지 않습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "아이디, 비밀번호의 형식과 어긋납니다.");

    private final HttpStatus httpStatus;
    private final String message;
}