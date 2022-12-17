package com.team1.rtback.service;

import com.team1.rtback.dto.user.SignUpRequestDto;
import com.team1.rtback.dto.user.SignUpResponseDto;
import com.team1.rtback.entity.User;
import com.team1.rtback.repository.UserRepository;
import com.team1.rtback.util.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;


class UserServiceTest {

    UserService userService;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;


    @Test                                                                                                               // 테스트를 나타내는 어노테이션
    @DisplayName("회원가입 정상 케이스")                                                                                 // 테스트 라벨링
    void signup() {
        // given -- 준비물


        String userId = "test0";
        String password = "testtest1234!";
        String username = "테스트코드용";

        public U

        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(
                userId,
                password,
                username
        );

        // when -- 테스트하려는 로직
        SignUpResponseDto signUpResponseDto = userService.signup(signUpRequestDto);

    }
}