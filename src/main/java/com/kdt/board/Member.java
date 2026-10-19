package com.kdt.board;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


// 회원 엔티티 (board_member 테이블)
@Entity
@Table(name = "board_member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 회원 PK (DB가 자동 증가)
    private Long id;

    // 로그인 ID: 필수, 중복 불가, 최대 20자
    @Column(nullable = false, unique = true, length = 20)
    private String loginId;

    // 닉네임: 필수, 최대 20자
    @Column(nullable = false, length = 20)
    private String nickname;

    public Member() {
    }

    // 이 회원이 쓴 게시글 목록 (회원 삭제 시 게시글도 함께 삭제)
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    // 게시글 목록에 게시글 추가
    public void addPost(Post post){
        this.posts.add(post);
    }

    public Member(String loginId, String nickname) {
        this.loginId = loginId;
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}