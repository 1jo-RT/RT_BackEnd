package com.team1.rtback.dto.global;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 1. 기능   : 종합 반환 클래스 설정
// 2. 작성자 : 서혁수
@Getter
@NoArgsConstructor
public class GlobalDto {
    private int status;
    private String msg;

    public GlobalDto(GlobalEnum globalEnum){
        this.status = globalEnum.getHttpStatus().value();
        this.msg = globalEnum.getMsg();
    }
}
