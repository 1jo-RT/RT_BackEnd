package com.team1.rtback.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team1.rtback.dto.user.LoginRequestDto;
import com.team1.rtback.dto.user.SignUpRequestDto;
import com.team1.rtback.service.KakaoService;
import com.team1.rtback.service.UserService;
import com.team1.rtback.util.jwt.JwtUtil;
import com.team1.rtback.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

// 1. 기능    : 회원 관련 종합 컨트롤러 (회원가입, 로그인)
// 2. 작성자  : 조소영
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    // 회원가입
    @PostMapping
    public ResponseEntity<?> signup(
            @RequestBody @Valid SignUpRequestDto signUpRequestDto){
        return new ResponseEntity<>(userService.signup(signUpRequestDto), HttpStatus.OK);
    }

    // 폼 로그인
    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        String createToken = userService.login(loginRequestDto, response);

        // Cookie 생성 및 직접 브라우저에 Set
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, createToken.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:http://localhost:3000";
    }

    // 회원탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete (@AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(userService.deleteUser(userDetails.getUser()), HttpStatus.OK);
    }

    // 카카오 로그인
    @GetMapping("/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        // 인가코드를 서비스로 전달
        String createToken = kakaoService.kakaoLogin(code, response);

        // Cookie 생성 및 직접 브라우저에 Set
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, createToken.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:http://localhost:3000";
    }
}
