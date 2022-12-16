package com.team1.rtback.service;

import com.team1.rtback.dto.user.SignUpRequestDto;
import com.team1.rtback.dto.user.SignUpResponseDto;
import com.team1.rtback.entity.User;
import com.team1.rtback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.team1.rtback.dto.global.SuccessCode.JOIN_OK;

// 1. 기능 : Spring Security 설정
// 2. 작성자 : 조소영
// 3. 생성날자 : 2022-12-16
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 기능 : 회원 가입
    public SignUpResponseDto signup (String userId, String username, String password){
        // 1. 중복 여부 검사
        if(userRepository.existsByUserId(userId)){
            throw new IllegalArgumentException("존재하는 아이디 입니다");
        }

        // 2. 암호화 및 저장
        User user = new User(userId, username, passwordEncoder.encode(password));
        userRepository.save(user);

        return new SignUpResponseDto(JOIN_OK);
    }
}
