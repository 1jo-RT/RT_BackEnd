package com.team1.rtback.dto.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class MsgResponseDto {
    private int statusCode;
    private String msg;
}
