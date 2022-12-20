package com.team1.rtback.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 1. 기능    : 게시글 작성 시 입력요소
// 2. 작성자  : 서혁수
@Getter
@ToString
@NoArgsConstructor
public class BoardRequestDto {
    private String title;
    private String content;
}
