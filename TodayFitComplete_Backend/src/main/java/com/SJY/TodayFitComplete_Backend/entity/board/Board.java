package com.SJY.TodayFitComplete_Backend.entity.board;

import com.SJY.TodayFitComplete_Backend.entity.common.BaseTimeEntity;
import com.SJY.TodayFitComplete_Backend.entity.file.FileEntity;
import com.SJY.TodayFitComplete_Backend.entity.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String content;

    @Column(nullable = false)
    private int viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memeber_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Member member;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    public List<FileEntity> files = new ArrayList<>();

    @Builder
    public Board(Long id, String title, String content, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.member = member;
    }

    public void upViewCount() {
        this.viewCount++;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
