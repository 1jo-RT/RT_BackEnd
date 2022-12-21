package com.team1.rtback.repository;

import com.team1.rtback.entity.Board;
import com.team1.rtback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 1. 기능    : 게시글 JPA 인터페이스
// 2. 작성자  : 서혁수
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 모든 게시글 내림차순 조회
    List<Board> findAllByOrderByCreatedAtDesc();

    // 유저 번호와 일치하는 모든 게시글 조회
    List<Board> findAllById(Long userIdx);

    // 유저 정보와 일치하는 모든 게시글 삭제
    void deleteAllByUser(User user);
}
