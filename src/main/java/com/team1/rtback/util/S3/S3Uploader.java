package com.team1.rtback.util.S3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.team1.rtback.entity.Board;
import com.team1.rtback.repository.BoardImageRepository;
import com.team1.rtback.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;
    private final BoardRepository boardRepository;

    @Value("${region}")
    public String bucket;

    @Value("${aws.url}")
    public String awsUrl;


    @Transactional
    public String boardImgUpload(MultipartFile multipartFile) throws IOException {
        // S3 Bucket 내부에 "static"이라는 이름의 디렉토리가 생성이 된다.
        return ImgUpload(multipartFile, "static");
    }

    @Transactional
    public String boardImgUpdate(MultipartFile multipartFile, Long boardId) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("없는 글임"));

        // 유저 이미지 변경이 없었을 경우 게시글 안의 기존 이미지를 리턴
        if (multipartFile.isEmpty()) {
            return board.getImgUrl();
        } else {
            // 변경의 요청이 있을 경우 기존 이미지를 s3 저장공간에서 삭제 한다.
            if (!board.getImgUrl().equals("")) {
                String filename = board.getImgUrl().substring(54);
                deleteFile(filename);
            }
            return ImgUpload(multipartFile, "static");
        }

    }

    public String ImgUpload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile).orElseThrow(
                () -> new IllegalArgumentException("파일 전환 실패"));
        return upload(uploadFile, dirName);
    }

    // S3로 파일 업로드하기
    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID();      // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName);       // s3로 업로드
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // S3에 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    // multipartFile 을 File 타입으로 변환 ( 변환된 파일을 이용해서 put 해준다. )
    private Optional<File> convert(MultipartFile multipartFile) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());
        // 바로 위에서 지정한 경로에 File 이 생성됨 (경로가 잘못되었다면 생성 불가능)
        if (convertFile.createNewFile()) {
            // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // 파일 삭제
    public void deleteFile(String fileName) {
        DeleteObjectRequest request = new DeleteObjectRequest(bucket, fileName);
        amazonS3Client.deleteObject(request);
    }
//    public void deleteFile(String fileName) {
//        amazonS3Client.deleteObject(bucket, fileName);
//    }
}