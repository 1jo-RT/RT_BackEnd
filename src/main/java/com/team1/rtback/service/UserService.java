package com.team1.rtback.service;

import com.team1.rtback.dto.global.GlobalDto;
import com.team1.rtback.dto.user.*;
import com.team1.rtback.entity.Board;
import com.team1.rtback.entity.User;
import com.team1.rtback.entity.UserRoleEnum;
import com.team1.rtback.repository.*;
import com.team1.rtback.exception.CustomException;
import com.team1.rtback.util.S3.S3Uploader;
import com.team1.rtback.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;


import java.util.List;

import static com.team1.rtback.exception.ErrorCode.*;

// 1. 기능   : 유저 서비스
// 2. 작성자 : 조소영
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final CommentRepository commentRepository;
    private final BoardImageRepository boardImageRepository;
    private final S3Uploader s3Uploader;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원 가입
    public void signup (SignUpRequestDto signUpRequestDto){
        // 1. 중복 여부 검사
        if(userRepository.existsByUserId(signUpRequestDto.getUserId())){
            throw new CustomException(DUPLICATED_USERID);
        }

        String encodePassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        // 2. 암호화 및 저장
        User user = new User(signUpRequestDto.getUserId(), signUpRequestDto.getUsername(), encodePassword, UserRoleEnum.USER);
        userRepository.save(user);

    }

    // 폼 로그인
    public void login (LoginRequestDto loginRequestDto, HttpServletResponse response){
        String userId = loginRequestDto.getUserId();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new CustomException(NOT_FOUND_USER)
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(NOT_MATCH_PASSWORD);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUserId(), user.getUsername(), UserRoleEnum.USER.getAuthority()));

    }

    @Transactional
    public void deleteUser(User user){
        // 유져 정보와 관련된 모든 이미지 s3 저장소에서 삭제
        List<Board> boardList = boardRepository.findAllById(user.getId());
        for (Board find : boardList) {
            if (!find.getImgUrl().equals(""))
                s3Uploader.deleteFile(find.getImgUrl().substring(54));
        }
        // 유저 정보와 관련된 모든 DB 삭제하기
        boardImageRepository.deleteAllByUserIdx(user.getId());
        commentRepository.deleteAllByUser(user);
        boardLikeRepository.deleteAllByUser(user);
        boardRepository.deleteAllByUser(user);
        userRepository.delete(user);
    }
}
