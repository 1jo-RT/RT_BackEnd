package com.team1.rtback.service;

import com.team1.rtback.dto.user.LoginRequestDto;
import com.team1.rtback.dto.user.LoginResponseDto;
import com.team1.rtback.dto.user.SignUpRequestDto;
import com.team1.rtback.dto.user.SignUpResponseDto;
import com.team1.rtback.entity.User;
import com.team1.rtback.exception.CustomException;
import com.team1.rtback.repository.UserRepository;
import com.team1.rtback.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

import static com.team1.rtback.dto.global.SuccessCode.JOIN_OK;
import static com.team1.rtback.dto.global.SuccessCode.LOGIN_OK;
import static com.team1.rtback.exception.ErrorCode.*;

// 1. 기능   : 유저 서비스
// 2. 작성자 : 조소영
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 기능 : 회원 가입
    public SignUpResponseDto signup (SignUpRequestDto signUpRequestDto){
        // 1. 중복 여부 검사
        if(userRepository.existsByUserId(signUpRequestDto.getUserId())){
            throw new CustomException(DUPLICATED_USERID);
        }

        String encodePassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        // 2. 암호화 및 저장
        User user = new User(signUpRequestDto.getUserId(), signUpRequestDto.getUsername(), encodePassword);
        userRepository.save(user);

        return new SignUpResponseDto(JOIN_OK);
    }

    public LoginResponseDto login (LoginRequestDto loginRequestDto, HttpServletResponse response){
        String userId = loginRequestDto.getUserId();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER)
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(NOT_MATCH_PASSWORD);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));

        return new LoginResponseDto(LOGIN_OK);
    }
}
