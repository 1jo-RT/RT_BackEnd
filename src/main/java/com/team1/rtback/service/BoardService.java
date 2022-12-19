package com.team1.rtback.service;

import com.team1.rtback.dto.board.BoardRequestDto;
import com.team1.rtback.dto.board.BoardResponseDto;
import com.team1.rtback.dto.comment.CommentResponseDto;
import com.team1.rtback.dto.global.GlobalDto;
import com.team1.rtback.entity.Board;
import com.team1.rtback.entity.BoardLike;
import com.team1.rtback.entity.Comment;
import com.team1.rtback.entity.User;
import com.team1.rtback.exception.CustomException;
import com.team1.rtback.repository.BoardLikeRepository;
import com.team1.rtback.repository.BoardRepository;
import com.team1.rtback.repository.CommentRepository;
import com.team1.rtback.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.team1.rtback.exception.ErrorCode.AUTHORIZATION;
import static com.team1.rtback.exception.ErrorCode.NOT_FOUND_BOARD;


// 1. 기능    : 게시글 서비스
// 2. 작성자  : 서혁수
@Builder
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BoardLikeRepository boardLikeRepository;


    // 전체 글 읽기
    public List<BoardResponseDto> getAllBoard() {

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
                result.add(new BoardResponseDto(board, commentList));
            }
        }

        return result;
    }

    // 게시글 읽기
    public List<BoardResponseDto> getBoard(Long boardId) {

        // 1. 요청한 글 존재 여부 확인
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD)
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
        result.add(new BoardResponseDto(board, commentList));

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
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        // 2. 글 작성자와 같은 계정인지 검증 후 수정
        if (user.getId() == board.getUser().getId()) {
            board.update(requestDto, user);
        } else
            throw new CustomException(AUTHORIZATION);

        return new BoardResponseDto(board, user.getId());
    }

    // 게시글 삭제
    public GlobalDto deleteBoard(Long boardId, User user) {

        // 1. 요청한 글 존재 여부 확인
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        // 2. 삭제 권한 확인
        if (user.getId() != board.getUser().getId())
            throw new CustomException(AUTHORIZATION);

        // 3. 요청한 글의 모든 댓글 리스트 가져오기
        List<Comment> commentList = commentRepository.findAllByBoard_IdOrderByCreatedAtDesc(boardId);

        // 4. 요청한 글의 모든 좋아요 리스트 가져오기
        List<BoardLike> boardLikeList = boardLikeRepository.findAllByBoardId(boardId);

        // 5. 모든 댓글 및 좋아요 삭제 후 글 삭제
        boardLikeRepository.deleteAll(boardLikeList);
        commentRepository.deleteAll(commentList);
        boardRepository.delete(board);

        return new GlobalDto(200, "삭제 완료");
    }

    // 게시글 좋아요
    @Transactional
    public GlobalDto likeCount(Long boardId, User user) {

        // 1. 요청한 글 존재 여부 확인
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        Optional<BoardLike> boardLike;
        boardLike = boardLikeRepository.findAllByBoardIdAndUserId(board.getId(), user.getId());


        if (boardLike.isEmpty()) {
            boardLikeRepository.save(new BoardLike(board, user));
            board.likeCount(board.getBoardLikeCount() + 1);
            return new GlobalDto(200, "좋아요 완료");
        } else {
            boardLikeRepository.deleteBoardLikeById(boardLike.get().getId());
            board.likeCount(board.getBoardLikeCount() - 1);
            return new GlobalDto(200, "좋아요 취소");
        }
    }
}
