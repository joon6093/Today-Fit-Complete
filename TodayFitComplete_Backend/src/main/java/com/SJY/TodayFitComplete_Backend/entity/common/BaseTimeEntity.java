package com.SJY.TodayFitComplete_Backend.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
public class BaseTimeEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private String createdDate; // 생성 날짜

    @LastModifiedDate
    @Column(nullable = false)
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

