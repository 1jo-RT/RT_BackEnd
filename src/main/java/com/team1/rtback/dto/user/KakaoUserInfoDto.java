package com.team1.rtback.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 1. 기능    : 카카오로부터 유저정보 받아오는 Dto
// 2. 작성자  : 조소영
@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String email;
    private String nickname;

    public KakaoUserInfoDto(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }

}