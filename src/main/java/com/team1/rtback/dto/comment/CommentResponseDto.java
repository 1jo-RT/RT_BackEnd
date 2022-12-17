package com.team1.rtback.dto.comment;

import com.team1.rtback.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class CommentResponseDto {
    private Long id;        //댓글 번호
    private String username;        //닉네임
    private String comment;         //댓글 내용
    private LocalDateTime createdAt;    //생성 일자
    private LocalDateTime modifiedAt;   //수정 일자

    public CommentResponseDto (Comment comment) {
        this.id = comment.getId();
        this.username = comment.getUsername();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
