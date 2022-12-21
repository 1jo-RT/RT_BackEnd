package com.team1.rtback.dto.global;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GlobalDto {
    private int statusCode;
    private String msg;

    public GlobalDto(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }
}
