package com.team1.rtback.repository;

import com.team1.rtback.entity.Board;
import com.team1.rtback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// 1. 기능    : 게시글 JPA 인터페이스
// 2. 작성자  : 서혁수
public interface BoardRepository extends JpaRepository<Board, Long> {

    // 모든 게시글 내림차순 조회
    List<Board> findAllByOrderByCreatedAtDesc();
    List<Board> deleteAllByUser(User user);
}
