package com.team1.rtback.repository;

import com.team1.rtback.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    Optional<BoardImage> findByBoardIdxAndUserIdx(Long boardIdx, Long userIdx);
}
