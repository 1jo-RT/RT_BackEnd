package com.team1.rtback.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {
    private Long userIdx;
    private String title;
    private String content;
    private String imgUrl;
}
