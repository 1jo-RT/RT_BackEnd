package com.team1.rtback.util.security;

import com.team1.rtback.entity.User;
import com.team1.rtback.exception.CustomException;
import com.team1.rtback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.team1.rtback.exception.ErrorCode.NOT_FOUND_USER;

// 1. 기능   : DB에서 정보 가져오기
// 2. 작성자 : 조소영
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
        구현 메소드 이름이 username이지만 userId를 통해 검색함
     **/
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new CustomException(NOT_FOUND_USER));

        return new UserDetailsImpl(user, user.getUserId());
    }
}