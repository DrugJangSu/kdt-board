package com.kdt.board;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

// 게시글 엔티티 (board_post 테이블)
@Entity
@Table(name = "board_post")
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 게시글 PK (DB가 자동 증가)
    private Long id;

    // 제목: 필수, 최대 20자
    @Column(nullable = false, length = 20)
    private String title;

    // 본문: 필수, 최대 100자
    @Column(nullable = false, length = 100)
    private String body;

    // 생성 시각 (최초 저장 시 자동 입력, 수정 불가)
    @CreatedDate
    @Column(nullable = false,updatable = false, name = "created_at")
    private Instant createdAt;


    // 생성 시각 읽기
    public Instant getCreatedAt() {
        return createdAt;
    }

    // 수정 시각 (저장/수정할 때마다 자동 갱신)
    @LastModifiedDate
    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;

    // 수정 시각 읽기
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // 기본 생성자 (JPA가 객체를 만들 때 꼭 필요해요)
    public Post() {
    }

    // 작성자(회원): 필수, 실제로 사용할 때 조회(지연 로딩)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 아래는 필드 값을 읽고(get) 바꾸는(set) 메서드들이에요.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}