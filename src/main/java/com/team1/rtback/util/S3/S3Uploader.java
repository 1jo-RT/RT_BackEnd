package com.team1.rtback.util.S3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
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

    public String imgName;

    @Transactional
    public String boardImgUpload(MultipartFile multipartFile) throws IOException {

        // S3 Bucket 내부에 "static"이라는 이름의 디렉토리가 생성이 된다.
        if (!multipartFile.isEmpty()) {
            return ImgUpload(multipartFile, "static");
        }

        return null;
    }

/*    @Transactional
    public String boardImgUpdate(MultipartFile multipartFile , Long boardId, User user) throws IOException {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("없는 글임"));

        if (user.getId() == board.getUser().getId()) {
            return ImgUpload(multipartFile, "static");
        }

        return null;
    }*/

    public String ImgUpload(MultipartFile multipartFile, String dirName) throws IOException{
        File uploadFile = convert(multipartFile).orElseThrow(
                () -> new IllegalArgumentException("파일 전환 실패"));
        return upload(uploadFile, dirName);
    }

    // S3로 파일 업로드하기
    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();     // S3에 저장된 파일 이름
        imgName = fileName;
        String uploadImageUrl = putS3(uploadFile, fileName);                             // s3로 업로드
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
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

    //
    private Optional<File> convert(MultipartFile multipartFile) throws IOException{
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
}