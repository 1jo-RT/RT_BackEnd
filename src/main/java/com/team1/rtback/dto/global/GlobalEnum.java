package com.team1.rtback.dto.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 1. 기능   : 성공 메세지 커스텀
// 2. 작성자 : 조소영
// 추가) 1. 기능 : 메시지 종류 추가,  2. 작성자 : 박영준
@Getter
@AllArgsConstructor
public enum GlobalEnum {

    JOIN_OK(HttpStatus.OK, "회원가입 성공"),
    LOGIN_OK(HttpStatus.OK, "로그인 성공"),
    THUMBNAIL_UPLOAD_OK(HttpStatus.OK, "프로필 이미지 업로드 완료"),
    USER_DELETE_OK(HttpStatus.OK, "탈퇴 완료"),
    COMMENT_NEW_OK(HttpStatus.OK, "댓글 작성 완료"),
    COMMENT_MODIFIED_OK(HttpStatus.OK, "댓글 수정 완료"),
    COMMENT_DELETE_OK(HttpStatus.OK, "댓글 삭제 완료"),

    BOARD_NEW_OK(HttpStatus.OK, "게시글 작성 완료"),
    BOARD_MODIFIED_OK(HttpStatus.OK, "게시글 수정 완료"),
    BOARD_DELETE_OK(HttpStatus.OK, "게시글 삭제 완료"),
    BOARD_LIKE_OK(HttpStatus.OK, "게시글 좋아요 완료"),
    BOARD_LIKE_CANCEL(HttpStatus.OK, "게시글 좋아요 취소 완료");


    private final HttpStatus httpStatus;
    private final String msg;
}
