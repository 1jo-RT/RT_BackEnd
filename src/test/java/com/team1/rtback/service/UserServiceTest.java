package com.team1.rtback.service;

import com.team1.rtback.dto.global.SuccessCode;
import com.team1.rtback.dto.user.SignUpRequestDto;
import com.team1.rtback.dto.user.SignUpResponseDto;
import com.team1.rtback.entity.User;
import com.team1.rtback.repository.UserRepository;
import com.team1.rtback.util.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.team1.rtback.dto.global.SuccessCode.JOIN_OK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class) // 확장 기능 구현
class UserServiceTest {

    @Mock   // 가짜 객체, 테스트 run시 실제 객체가 아닌 Mock객체 ㅏㄴ환
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @InjectMocks                        // Mock 객체가 주입될 클래스
    UserService userService;

//    @Spy // 실제 인스턴스 사용

    @Test                                                                                                               // 테스트를 나타내는 어노테이션
    @DisplayName("[User][Sucess] 회원가입")                                                                                 // 테스트 라벨링
    void signup() {
        // given -- 준비물
        String userId = "test0";
        String password = "testtest1234!";
        String username = "테스트코드용";

        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(userId, password, username);

        // when -- 테스트하려는 로직
        SignUpResponseDto result = userService.signup(signUpRequestDto);


        // then
        assertEquals(result, SignUpResponseDto(JOIN_OK));
    }
}