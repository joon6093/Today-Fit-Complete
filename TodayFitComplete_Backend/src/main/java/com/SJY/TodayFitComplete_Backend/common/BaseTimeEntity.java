package com.SJY.TodayFitComplete_Backend.common;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    @Column(name = "create_date", updatable = false)
    private String createdDate; // 생성 날짜

    @LastModifiedDate
    @Column(name = "modified_date")
    private String modifiedDate; // 수정 날짜

    /**
     * 엔티티 저장 전에 호출되는 메소드. 생성 및 수정 날짜 설정.
     */
    @PrePersist
    public void onPrePersist() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        this.createdDate = now;
        this.modifiedDate = now;
    }

    /**
     * 엔티티 업데이트 전에 호출되는 메소드. 수정 날짜 업데이트.
     */
    @PreUpdate
    public void onPreUpdate() {
        this.modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }
}

