package com.team1.rtback.service;

import com.team1.rtback.dto.board.BoardRequestDto;
import com.team1.rtback.dto.board.BoardResponseDto;
import com.team1.rtback.dto.comment.CommentResponseDto;
import com.team1.rtback.dto.global.GlobalDto;
import com.team1.rtback.dto.global.MsgResponseDto;
import com.team1.rtback.entity.Board;
import com.team1.rtback.entity.BoardLike;
import com.team1.rtback.entity.Comment;
import com.team1.rtback.entity.User;
import com.team1.rtback.repository.BoardLikeRepository;
import com.team1.rtback.repository.BoardRepository;
import com.team1.rtback.repository.CommentRepository;
import com.team1.rtback.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

// 1. 기능    : 게시글 서비스
// 2. 작성자  : 서혁수
// 추가) 1. 기능 : 게시글 좋아요,  2. 작성자 : 박영준
@Builder
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BoardLikeRepository boardLikeRepository;

    // 전체 글 읽기
    public List<BoardResponseDto> getAllBoard(User user) {

//        List<Board> boardList = boardRepository.findAll();
//
//        ArrayList<BoardResponseDto> result = new ArrayList<>();
//
//        for (Board board : boardList) {
//
//            result.add(new BoardResponseDto(board));
//        }
        // 1. 모든 글 정보를 가지고 온다.
        // List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        // 위처럼 내림차순 정렬의 다른 방법도 존재한다.
        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();
        List<BoardResponseDto> result = new ArrayList<>();

        // 2. 글이 한 개 이상이면 총 갯수 만큼 반복문 시작
        if (boardList.size() > 0) {
            for (Board board : boardList) {
                // 3. 해당 글의 모든 댓글을 가져온다.
                List<Comment> comments = commentRepository.findAllByBoard_IdOrderByCreatedAtDesc(board.getId());
                List<CommentResponseDto> commentList = new ArrayList<>();

                // 4. 해당 글에 존재하는 모든 댓글을 담은 리스트에 넣어준다.
                if (comments.size() > 0) {
                    for (Comment comment : comments) {
                        commentList.add(new CommentResponseDto(comment));
                    }
                }

                // 5. 최종적으로 하나의 리스트로 형성해서 하나씩 result 에 담는다.
                result.add(new BoardResponseDto(board, commentList, checkBoardLike(board.getId(), user)));
            }
        }

        return result;
    }

    // 게시글 읽기
    public List<BoardResponseDto> getBoard(Long boardId, User user) {

        // 1. 요청한 글 존재 여부 확인
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("없는 글임")
        );

        // 2. 요청한 글의 댓글을 내림차순으로 가져온다.
        List<Comment> comments = commentRepository.findAllByBoard_IdOrderByCreatedAtDesc(boardId);

        // 3. 글과 댓글을 담기 위한 배열 선언
        List<BoardResponseDto> result = new ArrayList<>();
        List<CommentResponseDto> commentList = new ArrayList<>();

        // 4. 댓글 갯수가 0개 이상일 때 부터 댓글 리스트에 넣어준다.
        if (comments.size() > 0) {
            for (Comment comment : comments) {
                commentList.add(new CommentResponseDto(comment));
            }
        }

        // 5. 리스트로 반환하기 위해서 result 넣어준다.
        result.add(new BoardResponseDto(board, commentList, checkBoardLike(board.getId(), user)));

        return result;
    }

    // 게시글 작성
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto, User user) {
        Board board = new Board(requestDto, user);
        boardRepository.save(board);
        return new BoardResponseDto(board, user.getId());
    }

    // 게시글 수정
    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto, User user) {

        // 1. 요청한 글 존재 여부 확인
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("없는 글임")
        );

        // 2. 글 작성자와 같은 계정인지 검증 후 수정
        if (user.getId() == board.getUser().getId()) {
            board.update(requestDto, user);
        } else
            throw new IllegalArgumentException("계정 불일치");

        return new BoardResponseDto(board, user.getId());
    }

    // 게시글 삭제
    public GlobalDto deleteBoard(Long boardId, User user) {

        // 1. 요청한 글 존재 여부 확인
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("없는 글임")
        );

        // 2. 삭제 권한 확인
        if (user.getId() != board.getUser().getId())
            throw new IllegalArgumentException("님 글 아님");

        // 3. 요청한 글의 모든 댓글 리스트 가져오기
        List<Comment> commentList = commentRepository.findAllByBoard_IdOrderByCreatedAtDesc(boardId);

        // 4. 모든 댓글 삭제 후 글 삭제
        commentRepository.deleteAll(commentList);
        boardRepository.delete(board);

        return new GlobalDto(200, "삭제 완료");
    }

    // 게시글 좋아요 확인
    @Transactional(readOnly = true)
    public boolean checkBoardLike(Long boardId, User user) {
        // DB 에서 해당 유저의 게시글 좋아요 여부 확인
        return boardLikeRepository.existsByBoardIdAndUserId(boardId, user.getId());
    }

    // 게시글 좋아요 생성 및 삭제
    @Transactional
    public MsgResponseDto createBoardLike(Long boardId, User user) {
        // 1. 요청한 글이 DB 에 존재하는지 확인
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("없는 글임")
        );

        // 2. 해당 유저의 게시글 좋아요 여부를 확인해서 false 라면
        if (!checkBoardLike(boardId, user)) {
            // 3. 즉시 해당 유저의 게시글 좋아요를 DB 에 저장
            boardLikeRepository.saveAndFlush(new BoardLike(board, user));
            return new MsgResponseDto(HttpStatus.OK.value(), "게시글 좋아요 완료");
        // 4. 게시글 좋아요 여부가 true 라면, 해당 유저의 게시글 좋아요를 DB 에서 삭제
        } else {
            boardLikeRepository.deleteByBoardIdAndUserId(boardId, user.getId());
            return new MsgResponseDto(HttpStatus.OK.value(), "게시글 좋아요 취소");
        }
    }
}
