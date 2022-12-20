package com.team1.rtback.repository;

import com.team1.rtback.entity.Board;
import com.team1.rtback.entity.BoardLike;
import com.team1.rtback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    Optional<BoardLike> findAllByBoardIdAndUserId(Long boardId, Long userId);

    List<BoardLike> findAllByBoardId(Long boardId);

    void deleteBoardLikeById(Long id);

    List<BoardLike> deleteAllByUser(User user);
}
