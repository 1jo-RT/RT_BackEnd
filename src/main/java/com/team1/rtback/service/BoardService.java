package com.team1.rtback.service;

import com.team1.rtback.dto.board.BoardRequestDto;
import com.team1.rtback.dto.board.BoardResponseDto;
import com.team1.rtback.dto.comment.CommentResponseDto;
import com.team1.rtback.dto.global.GlobalDto;
import com.team1.rtback.entity.Board;
import com.team1.rtback.entity.BoardImage;
import com.team1.rtback.entity.Comment;
import com.team1.rtback.entity.User;
import com.team1.rtback.repository.BoardImageRepository;
import com.team1.rtback.entity.BoardLike;
import com.team1.rtback.exception.CustomException;
import com.team1.rtback.repository.BoardLikeRepository;
import com.team1.rtback.repository.BoardRepository;
import com.team1.rtback.repository.CommentRepository;
import com.team1.rtback.repository.UserRepository;
import com.team1.rtback.util.S3.S3Uploader;
import com.team1.rtback.util.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.team1.rtback.dto.global.GlobalEnum.*;
import static com.team1.rtback.exception.ErrorCode.*;

// 1. 기능    : 게시글 서비스
// 2. 작성자  : 서혁수
// 추가) 1. 기능 : 게시글 좋아요,  2. 작성자 : 박영준
//@Builder
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final S3Uploader s3Uploader;
    private final BoardImageRepository boardImageRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final JwtUtil jwtUtil;

    @Value("${aws.url}")
    public String awsUrl;

    // 모든 게시글 가져오기 (비로그인)
    public List<BoardResponseDto> getBoardListAll() {
        // 1. 모든 글 정보를 가지고 온다.
        // List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        // 위처럼 내림차순 정렬의 다른 방법도 존재한다.
        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();
        List<BoardResponseDto> result = new ArrayList<>();

        // 2. 글이 한 개 이상이면 총 갯수 만큼 반복문 시작
        if (boardList.size() > 0) {
            for (Board board : boardList) {
                // 3. 해당 글의 댓글을 내림차순으로 가져온다.
                List<Comment> comments = commentRepository.findAllByBoard_IdOrderByCreatedAtDesc(board.getId());
                List<CommentResponseDto> commentList = new ArrayList<>();

                // 4. 해당 글에 존재하는 모든 댓글을 담은 리스트에 넣어준다.
                if (comments.size() > 0) {
                    for (Comment comment : comments) {
                        commentList.add(new CommentResponseDto(comment));
                    }
                }

                // 5. 해당 게시글의 모든 댓글을 함께 담아서 result 에 추가
                result.add(new BoardResponseDto(board, commentList, false));
            }
        }

        return result;
    }

    // 단건 게시글 가져오기 (비로그인)
    public List<BoardResponseDto> getBoardList(Long boardId) {
        List<BoardResponseDto> result = new ArrayList<>();

        // 1. 요청한 글 존재 여부 확인
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD));

        // 2. 요청한 글의 댓글을 내림차순으로 가져온다.
        List<Comment> comments = commentRepository.findAllByBoard_IdOrderByCreatedAtDesc(boardId);
        List<CommentResponseDto> commentList = new ArrayList<>();

        // 4. 댓글 갯수가 0개 이상일 때 부터 댓글 리스트에 넣어준다.
        if (comments.size() > 0) {
            for (Comment comment : comments) {
                commentList.add(new CommentResponseDto(comment));
            }
        }

        // 5. 요청한 게시글의 모든 댓글을 함께 담아서 result 에 추가
        result.add(new BoardResponseDto(board, commentList, false));

        return result;
    }

    // 전체 게시글 가져오기 (로그인)
    public List<BoardResponseDto> getAllBoard(HttpServletRequest request) {
        // 1. 토큰 검증은 게시글에 좋아요를 했는지 안했는지 판별하기 위해서 존재한다.
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 2. 모든 게시글을 담고있는 boardList 와 새롭게 담을 빈 배열 result
        List<BoardResponseDto> boardList = getBoardListAll();
        List<BoardResponseDto> result = new ArrayList<>();

        // 3. 검증을 거친 경우에 유저가 좋아요를 했는지 안했는지에 따라 boolean 값으로 반환한다.
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new CustomException(INVALID_TOKEN);
            }

            User user = userRepository.findByUserId((String) claims.get("userId")).orElseThrow(
                    () -> new CustomException(NOT_FOUND_USER));

            // 4. 게시글 마다 좋아요 boolean 결과 값을 조회해서 해당 값을 새롭게 만든 배열에 모든 게시글 정보와 함께 넣어주기
            for (BoardResponseDto boardResponseDto : boardList) {
                boolean check = checkBoardLike(boardResponseDto.getId(), user.getId());
                result.add(new BoardResponseDto(boardResponseDto, boardResponseDto.getCommentList(), check));
            }
        } else {
            // 5. 비로그인 유저는 좋아요 boolean 결과 값이 false 로 모든 게시글 반환
            return boardList;
        }
        return result;
    }

    // 단건 게시글 가져오기 (로그인)
    public List<BoardResponseDto> getBoard(Long boardId, HttpServletRequest request) {
        // 1. 토큰 검증은 게시글에 좋아요를 했는지 안했는지 판별하기 위해서 존재한다.
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 2. 모든 게시글을 담고있는 boardList 와 새롭게 담을 빈 배열 result
        List<BoardResponseDto> boardList = getBoardList(boardId);
        List<BoardResponseDto> result = new ArrayList<>();

        // 3. 검증을 거친 경우에 유저가 좋아요를 했는지 안했는지에 따라 boolean 값으로 반환한다.
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new CustomException(INVALID_TOKEN);
            }

            User user = userRepository.findByUserId((String) claims.get("userId")).orElseThrow(
                    () -> new CustomException(NOT_FOUND_USER)
            );

            // 4. 요청한 게시글의 좋아요 boolean 값을 찾아서 check 에 넣어준다.
            BoardResponseDto boardResponseDto = boardList.get(0);
            boolean check = checkBoardLike(boardResponseDto.getId(), user.getId());

            // 5. 요청한 게시글의 모든 정보를 담아서 result 에 추가한다.
            result.add(new BoardResponseDto(boardResponseDto, boardResponseDto.getCommentList(), check));
        } else {
            // 6. 비로그인 유저는 좋아요 boolean 결과 값이 false 로 게시글 반환
            return boardList;
        }
        return result;
    }

    @Transactional
    // 게시글 작성
    public BoardResponseDto createBoard(BoardRequestDto requestDto, MultipartFile multipartFile, User user) throws IOException {
        // 1. 이미지 URL 을 담기 위한 빈 문자열
        String imgName;

        // 2. 이미지 업로드 서비스 작동
        if (!multipartFile.isEmpty()) {
            imgName = s3Uploader.boardImgUpload(multipartFile);
        } else {
            imgName = "";
        }

        // 3. 새로운 게시글 생성 및 DB 생성
        Board board = new Board(requestDto, user, imgName);

        // 세이브가 리턴을 해줌으로 인해서 boardResult 에서 게시글 번호를 가져올 수 있게 된다.
        // 여기서 말하는 리턴은 세이브가 저장해준 값을 리턴해준다고 한다.
        Board boardResult = boardRepository.save(board);

        // 4. 새로운 게시글 이미지 생성 및 DB 생성
        BoardImage boardImage = new BoardImage(boardResult.getId(), user.getId(), imgName);
        boardImageRepository.save(boardImage);

        return new BoardResponseDto(board, user.getId());
    }

    // 게시글 수정
    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto, MultipartFile multipartFile, User user) throws IOException {
        String imgName;

        // 1. 요청한 글 존재 여부 확인 및 가져오기
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD));

        // 2. 요청한 이미지 테이블 존재 확인 및 가져오기
        BoardImage boardImage = boardImageRepository.findByUserIdxAndBoardIdx(user.getId(), board.getId()).orElseThrow(
                () -> new CustomException(AUTHORIZATION));

        // 3. 글 작성자와 같은 계정인지 검증 후 수정
        if (user.getId().equals(board.getUser().getId())) {
            board.update(requestDto, user);
        } else
            throw new CustomException(AUTHORIZATION);

        // 4. 이미지 s3 수정 업로드 서비스 실행
        imgName = s3Uploader.boardImgUpdate(multipartFile, boardId);

        // 5. 게시글 및 이미지 테이블 수정하기
        board.update(requestDto, user, imgName);
        boardImage.update(imgName);

        return new BoardResponseDto(board, user.getId());
    }

    // 게시글 삭제
    public ResponseEntity<GlobalDto> deleteBoard(Long boardId, User user) {

        // 1. 요청한 글 존재 여부 확인
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD)
        );

        // 2. 삭제 권한 확인
        if (!user.getId().equals(board.getUser().getId()))
            throw new CustomException(AUTHORIZATION);

        // 3. 요청한 글의 모든 댓글 리스트 가져오기
        List<Comment> commentList = commentRepository.findAllByBoard_IdOrderByCreatedAtDesc(boardId);

        // 4. 삭제 로직
        if (!board.getImgUrl().equals(""))
            s3Uploader.deleteFile(board.getImgUrl().substring(54));

        // 5. 요청한 글의 모든 좋아요 리스트 가져오기
        List<BoardLike> boardLikeList = boardLikeRepository.findAllByBoardId(boardId);

        // 6. 모든 댓글 및 좋아요 삭제 후 글 삭제
        boardLikeRepository.deleteAll(boardLikeList);

        commentRepository.deleteAll(commentList);
        boardRepository.delete(board);
        boardImageRepository.deleteById(boardId);

        return ResponseEntity.ok().body(new GlobalDto(BOARD_DELETE_OK));
    }

    // 유저의 게시글 좋아요 체크
    @Transactional(readOnly = true)
    public boolean checkBoardLike(Long boardId, Long userId) {
        // DB 에서 해당 유저의 게시글 좋아요 여부 확인
        return boardLikeRepository.existsByBoardIdAndUserId(boardId, userId);
    }

    // 게시글 좋아요 생성 및 삭제
    @Transactional
    public ResponseEntity<GlobalDto> createBoardLike(Long boardId, User user) {
        // 1. 요청한 글이 DB 에 존재하는지 확인
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new CustomException(NOT_FOUND_BOARD));

        // 2. 해당 유저의 게시글 좋아요 여부를 확인해서 false 라면
        if (!checkBoardLike(boardId, user.getId())) {
            // 3. 즉시 해당 유저의 게시글 좋아요를 DB 에 저장
            boardLikeRepository.saveAndFlush(new BoardLike(board, user));
            return ResponseEntity.ok().body(new GlobalDto(BOARD_LIKE_OK));
            // 4. 게시글 좋아요 여부가 true 라면, 해당 유저의 게시글 좋아요를 DB 에서 삭제
        } else {
            boardLikeRepository.deleteByBoardIdAndUserId(boardId, user.getId());
            return ResponseEntity.ok().body(new GlobalDto(BOARD_LIKE_CANCEL));
        }
    }

}
