package com.team1.rtback.dto.user;

import com.team1.rtback.dto.global.SuccessCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpResponseDto {
    private int status;
    private String msg;

    public SignUpResponseDto(SuccessCode successCode){
        this.status = successCode.getHttpStatus().value();
        this.msg = successCode.getMsg();

    }
}
