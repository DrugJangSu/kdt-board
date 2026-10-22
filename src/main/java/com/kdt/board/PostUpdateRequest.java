package com.kdt.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 게시글을 수정할 때 클라이언트가 보내는 요청 데이터를 담는 상자예요.
public record PostUpdateRequest(
        // 제목은 비어 있으면 안 되고 최대 20자까지만 허용해요.
        @NotBlank
        @Size(max = 20)
        String title,

        // 본문은 비어 있으면 안 되고 최대 100자까지만 허용해요.
        @NotBlank
        @Size(max = 100)
        String body
) {
}