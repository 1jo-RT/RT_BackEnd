package com.team1.rtback.repository;

import com.team1.rtback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 1. 기능   : 유저 JPA 인터페이스
// 2. 작성자 : 조소영
public interface UserRepository extends JpaRepository<User, Long> {

    // 유저 조회
    Optional<User> findById(Long userId);

    // 유저 존재여부 조회
    boolean existsByUserId(String userId);

    // 유저 아이디 조회
    Optional<User> findByUserId(String userId);

    // 유저 카카오 아이디 조회
    Optional<User> findByKakaoId(Long id);

    // 유저 이메일 조회
    Optional<User> findByEmail(String email);

}
