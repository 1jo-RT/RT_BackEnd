package com.team1.rtback.controller;

import com.team1.rtback.dto.user.LoginRequestDto;
import com.team1.rtback.dto.user.SignUpRequestDto;
import com.team1.rtback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

// 1. 기능 : 회원 관련 종합 컨트롤러 (회원가입, 로그인)
// 2. 작성자 : 조소영
// 3. 생성날자 : 2022-12-16
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping ("/")
    public ResponseEntity<?> signup(
            @RequestParam(value = "id") @Valid String userId,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") @Valid String password ){ // 이거 고쳐야함 RequestBody

        // 정규식 검사
        @Size(min=4, max=10, message = "4자에서 10자 사이")
        @Pattern(regexp="^[a-z0-9]*$", message = "알파벳 소문자 a-z 숫자 0-9 가능")
        String validUserId = userId;

        @Size(min=8, max=15, message = "8자에서 15자 사이")
        @Pattern(regexp="^[a-zA-Z0-9!@#$%^&+=]*$", message = "알파벳 소문자 a-z 숫자 0-9 특수문자 가능")
        String validPassword = password;


        return new ResponseEntity<>(userService.signup(validUserId, username, validPassword), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        return new ResponseEntity<>(userService.login(loginRequestDto, response), HttpStatus.OK);
    }

}
