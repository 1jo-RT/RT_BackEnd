package com.team1.rtback.dto.user;

import com.team1.rtback.dto.global.SuccessCode;
import lombok.Getter;

@Getter
public class KakaoResponseDto {
    private int status;
    private String msg;

    public KakaoResponseDto(SuccessCode successCode){
        this.status = successCode.getHttpStatus().value();
        this.msg = successCode.getMsg();

    }
}
