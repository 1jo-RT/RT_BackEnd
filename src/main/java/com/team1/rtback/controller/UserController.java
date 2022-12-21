package com.team1.rtback.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.team1.rtback.dto.user.LoginRequestDto;
import com.team1.rtback.dto.user.SignUpRequestDto;
import com.team1.rtback.service.KakaoService;
import com.team1.rtback.service.UserService;
import com.team1.rtback.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

//        System.out.println("=========================== UserId = " + signUpRequestDto.getUserId());
//        System.out.println("=========================== UserName = " + signUpRequestDto.getUsername());
//        System.out.println("=========================== Password = " + signUpRequestDto.getPassword());

        return new ResponseEntity<>(userService.signup(signUpRequestDto), HttpStatus.OK);
    }

    // 폼 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){

//        System.out.println("=========================== UserId = " + loginRequestDto.getUserId());
//        System.out.println("=========================== Password = " + loginRequestDto.getPassword());

        return new ResponseEntity<>(userService.login(loginRequestDto, response), HttpStatus.OK);
    }

    // 회원탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete (@AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(userService.deleteUser(userDetails.getUser()), HttpStatus.OK);
    }

    // 카카오 로그인
    @GetMapping("/kakao/callback")
    public ModelAndView kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // code: 카카오 서버로부터 받은 인가 코드
        // 인가코드를 서비스로 전달
        kakaoService.kakaoLogin(code, response);

//        // Cookie 생성 및 직접 브라우저에 Set
//        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, createToken.substring(7));
//        cookie.setPath("/");
//        response.addCookie(cookie);

        return new ModelAndView("redirect:http://localhost:3000/api/user/kakao/callback");
    }
}
