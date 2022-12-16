package com.team1.rtback.controller;

import com.team1.rtback.dto.user.SignUpRequestDto;
import com.team1.rtback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping ("/")
    public ResponseEntity<?> signup(
            @RequestParam(value = "id") @Valid String userId,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password ){

        // 정규식 검사
        @Size(min=4, max=10, message = "4자에서 10자 사이")
        @Pattern(regexp="^[a-z0-9]*$", message = "알파벳 소문자 a-z 숫자 0-9 가능")
        String validUserId = userId;

        @Size(min=8, max=15, message = "8자에서 15자 사이")
        @Pattern(regexp="^[a-zA-Z0-9!@#$%^&+=]*$", message = "알파벳 소문자 a-z 숫자 0-9 특수문자 가능")
        String validPassword = password;


        return new ResponseEntity<>(userService.signup(validUserId, username, validPassword), HttpStatus.OK);
    }

}