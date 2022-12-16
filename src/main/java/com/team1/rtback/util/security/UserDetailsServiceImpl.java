package com.team1.rtback.util.security;

import com.team1.rtback.entity.User;
import com.team1.rtback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 1. 기능 : DB에서 정보 가져오기
// 2. 작성자 : 조소영
// 3. 생성날자 : 2022-12-16
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalAccessError("해당 회원 정보를 불러올 수 없습니다."));

        return new UserDetailsImpl(user, user.getUsername());
    }
}