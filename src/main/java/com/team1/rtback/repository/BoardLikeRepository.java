package com.team1.rtback.repository;

import com.team1.rtback.entity.BoardLike;
import com.team1.rtback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// 1. 기능    : 게시글 좋아요 JPA 인터페이스
// 2. 작성자  : 박영준
public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    // 게시글 번호와 유저
    void deleteByBoardIdAndUserId(Long boardId, Long userId);

    // 게시글 번호와 유저 번호가 일치하면 true 를 뱉는다.
    boolean existsByBoardIdAndUserId(Long boardId, Long userId);

    // 게시글 번호와 일치하는 모든 게시글의 리스트를 받아온다.
    List<BoardLike> findAllByBoardId(Long boardId);

    // 유저 정보와 일치하는 모든 게시글 좋아요 삭제
    void deleteAllByUser(User user);
}
