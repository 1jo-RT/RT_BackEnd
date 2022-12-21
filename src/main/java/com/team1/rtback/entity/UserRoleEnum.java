package com.team1.rtback.entity;

// 1. 기능   : 유저 권한 구성요소
// 2. 작성자 : 조소영
public enum UserRoleEnum {
    USER(Authority.USER),       // 사용자 권한
    ADMIN(Authority.ADMIN);     // 관리자 권한

    private final String authority;

    UserRoleEnum (String authority){
        this.authority = authority;
    }

    public String getAuthority(){
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }

}
