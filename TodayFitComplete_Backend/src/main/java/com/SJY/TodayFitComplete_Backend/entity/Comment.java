package com.SJY.TodayFitComplete_Backend.entity;

import com.SJY.TodayFitComplete_Backend.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    public Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    public Board board;

    @Builder
    public Comment(Long id, String content, Member member, Board board) {
        this.id = id;
        this.content = content;
        this.member = member;
        this.board = board;
    }

    public void update(String content) {
        this.content = content;
    }

    public void setBoard(Board board) {
        this.board = board;
        board.getComments().add(this);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getComments().add(this);
    }
}
