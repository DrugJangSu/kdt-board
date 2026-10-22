package com.kdt.board;

import java.time.Instant;

// 게시글 한 개를 클라이언트에게 보여줄 때 쓰는 응답 데이터예요. (DB 엔티티 Post를 그대로 노출하지 않으려고 따로 만들어요)
public record PostResponse(Long id, String title, String loginId, Instant createdAt) {

    // post를 매개변수로 받아 PostResponse 변환하는 정적메서드
    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getMember().getLoginId(), post.getCreatedAt());
    }
}