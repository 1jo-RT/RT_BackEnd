package com.team1.rtback.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 1. 기능   : AWS s3 설정
// 2. 작성자 : 서혁수
@Configuration
public class AWSConfig {

    /**
     * key 는 중요 정보이기 때문에 properties 파일에 저장한 뒤 가져와 사용하도록 하자
     */
    @Value("${iamAccessKey}")
    private String iamAccessKey;

    @Value("${iamSecretkey}")
    private String iamSecretKey;

    private String region = "ap-northeast-2";

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(iamAccessKey, iamSecretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
