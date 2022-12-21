package com.team1.rtback.repository;

import com.team1.rtback.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 1. 기능    : 게시글 이미지 JPA 인터페이스
// 2. 작성자  : 서혁수
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    // 게시글 번호와 일치하는 유저 번호를 가진 모든 게시글 내림차순 조회
    Optional<BoardImage> findByUserIdxAndBoardIdx(Long userIdx, Long boardIdx);

    // 유저 고유번호와 일치하는 모든 게시글 삭제
    void deleteAllByUserIdx(Long userIdx);
}
