package com.team1.rtback.repository;

import com.team1.rtback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 1. 기능   : Spring Security 설정
// 2. 작성자 : 조소영
public interface UserRepository extends JpaRepository<User, Long> {

    // 유저 조회
    Optional<User> findById(Long userId);

    // 유저 존재여부 조회
    boolean existsByUserId(String userId);

    // 유저 닉네임 조회
    Optional<User> findByUsername(String username);

    // 유저 고유번호 조회
    Optional<User> findByUserId(String userId);

    Optional<User> findByKakaoId(Long id);
    Optional<User> findByEmail(String email);

}
