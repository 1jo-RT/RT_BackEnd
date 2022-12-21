package com.team1.rtback.repository;

import com.team1.rtback.entity.Board;
import com.team1.rtback.entity.BoardLike;
import com.team1.rtback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// 1. 기능    : 게시글 좋아요 JPA 인터페이스
// 2. 작성자  : 박영준
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    Optional<BoardLike> findAllByBoardIdAndUserId(Long boardId, Long userId);

    void deleteByBoardIdAndUserId(Long boardId, Long userId);
    
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);

    List<BoardLike> findAllByBoardId(Long boardId);

    void deleteBoardLikeById(Long id);

    List<BoardLike> deleteAllByUser(User user);
}
