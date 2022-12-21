package com.team1.rtback.dto.user;

import com.team1.rtback.dto.global.GlobalEnum;
import lombok.Getter;

// 1. 기능   : 카카오톡 반환 Dto
// 2. 작성자 : 조소영
@Getter
public class KakaoResponseDto {
    private int status;
    private String msg;

    public KakaoResponseDto(GlobalEnum globalEnum){
        this.status = globalEnum.getHttpStatus().value();
        this.msg = globalEnum.getMsg();

    }
}
