package com.team1.rtback.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// 1. 기능    : 게시글, 댓글 작성 시 날자 및 시간정보 자동 입력
// 2. 작성자  : 조소영
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Timestamped {

    // 객체의 입장에서 공통 매핑 정보가 필요할 때 사용
    // 공통 매핑 정보가 필요할 때, 부모 클래스에 선언하고, 속성만 상속 받아서 사용하고 싶을 때 --> Board 가 Timestamped 를 상속받고있다!
    // @EntityListeners(AuditingEntityListener.class) : 해당 클래스에 Auditing 기능(시간에 대해서 자동으로 값을 넣어주는 기능)을 포함
    // Entity 가 삽입, 삭제, 수정, 조회 등...의 작업을 하기 전/후에 어떤 작업을 하기 위해 이벤트 처리를 위해서
    // 예시: 데이터 삽입 전 또는 수정 전에 createdAt 과 modifiedAt 의 시간을 조작

    // 필드
    // createdAt, modifiedAt 컬럼 2개를 가진다
    // 생성일자
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;                    // 작성 날자

    // 마지막 수정일자
    @LastModifiedDate
    @Column
    private LocalDateTime modifiedAt;                   // 수정 날자
}

