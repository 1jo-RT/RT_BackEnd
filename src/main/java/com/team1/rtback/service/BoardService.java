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
import com.team1.rtback.repository.BoardRepository;
import com.team1.rtback.repository.CommentRepository;
import com.team1.rtback.repository.UserRepository;
import com.team1.rtback.util.S3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


// 1. 기능    : 게시글 서비스
// 2. 작성자  : 서혁수
//@Builder
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final S3Uploader s3Uploader;
    private final BoardImageRepository boardImageRepository;

    @Value("${aws.url}")
    public String awsUrl;

    // 전체 글 읽기
    public List<BoardResponseDto> getAllBoard() {

/*        List<Board> boardList = boardRepository.findAll();

        ArrayList<BoardResponseDto> result = new ArrayList<>();

        for (Board board : boardList) {

            result.add(new BoardResponseDto(board));
        }*/
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
        result.add(new BoardResponseDto(board, commentList));

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

        // 1. 요청한 글 존재 여부 확인
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("없는 글임"));

        BoardImage boardImage = boardImageRepository.findByBoardIdxAndUserIdx(board.getId(), user.getId()).orElseThrow(
                () -> new IllegalArgumentException("없는 정보"));

        // 2. 글 작성자와 같은 계정인지 검증
        if (user.getId() != board.getUser().getId())
            throw new IllegalArgumentException("계정 불일치");

//        if (!board.getImgUrl().equals("")) {
//            imgName = s3Uploader.boardImgUpload(multipartFile);
//            board.update(requestDto, user, imgName);
//        }
        // 3. 이미지 s3 수정 업로드 서비스 실행
        imgName = s3Uploader.boardImgUpdate(multipartFile, boardId);

        // 4. 게시글 및 이미지 테이블 업데이트
        board.update(requestDto, user, imgName);
        boardImage.update(imgName);

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
        if (!board.getImgUrl().equals(""))
            s3Uploader.deleteFile(board.getImgUrl().substring(54));

        commentRepository.deleteAll(commentList);
        boardRepository.delete(board);
        boardImageRepository.deleteById(boardId);

        return new GlobalDto(200, "삭제 완료");
    }

}
