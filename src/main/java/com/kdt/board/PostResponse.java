package com.kdt.board;

import java.time.Instant;

public record PostResponse(Long id, String title, String loginId, Instant createdAt) {

    // post를 매개변수로 받아 PostResponse 변환하는 정적메서드
    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getMember().getLoginId(), post.getCreatedAt());
    }
}