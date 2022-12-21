package com.team1.rtback.repository;

import com.team1.rtback.entity.Comment;
import com.team1.rtback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// 1. 기능    : 댓글 JPA 인터페이스
// 2. 작성자  : 박영준
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 댓글 작성자 확인
    Optional<Comment> findByIdAndUserId(Long cmtId, Long UserId);

    // 모든 댓글 내림차순 조회
    List<Comment> findAllByBoard_IdOrderByCreatedAtDesc(Long boardId);

    // 유저 정보와 일치하는 모든 댓글 삭제
    void deleteAllByUser(User user);

}