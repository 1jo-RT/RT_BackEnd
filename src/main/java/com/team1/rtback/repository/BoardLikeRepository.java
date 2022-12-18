package com.team1.rtback.repository;

import com.team1.rtback.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

// 1. 기능    : 게시글 좋아요 JPA 인터페이스
// 2. 작성자  : 박영준
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    void deleteByBoardIdAndUserId(Long boardId, Long userId);
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);
}