package com.team1.rtback.repository;

import com.team1.rtback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 1. 기능 : Spring Security 설정
// 2. 작성자 : 조소영
// 3. 생성날자 : 2022-12-16
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserId(String userId);
    Optional<User> findByUserId(String userId);
}
