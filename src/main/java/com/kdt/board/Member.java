package com.kdt.board;

import jakarta.persistence.*;

@Entity
@Table(name = "board_member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true,length = 20)
    private String loginId;

    @Column(nullable = false,length = 20)
    private String nickname;

    public Member() {
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