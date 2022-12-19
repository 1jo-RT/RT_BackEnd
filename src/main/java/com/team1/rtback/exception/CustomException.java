package com.team1.rtback.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 1. 기능    : 예외처리
// 2. 작성자  : 박영준
@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{      //전역으로 사용될 예외처리
    private final ErrorCode errorCode;
}