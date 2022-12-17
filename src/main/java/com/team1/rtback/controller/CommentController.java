package com.team1.rtback.controller;

import com.team1.rtback.dto.comment.CommentRequestDto;
import com.team1.rtback.dto.global.MsgResponseDto;
import com.team1.rtback.service.CommentService;
import com.team1.rtback.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards/{boardId}")

public class CommentController {
    private final CommentService commentService;

    //댓글 작성
    @PostMapping        //<CommentResponseDto>??  <MsgResponseDto>??
    public ResponseEntity<MsgResponseDto> createComment(@PathVariable Long boardId,
                                                        @RequestBody CommentRequestDto commentRequestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.createComment(boardId, commentRequestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new MsgResponseDto(HttpStatus.OK.value(), "댓글 작성 완료"));
    }

    //댓글 수정
    @PutMapping("/{commentId}")     //<CommentResponseDto>??  <MsgResponseDto>??
    public ResponseEntity<MsgResponseDto> updateComment(@PathVariable Long boardId, @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.updateComment(boardId, commentId, commentRequestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new MsgResponseDto(HttpStatus.OK.value(), "댓글 수정 완료"));
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<MsgResponseDto> deleteComment(@PathVariable Long boardId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(boardId, commentId, userDetails.getUser());
        return ResponseEntity.ok().body(new MsgResponseDto(HttpStatus.OK.value(), "댓글 삭제 완료"));
    }
}
