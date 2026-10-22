package com.kdt.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostCreateRequest(
        @NotBlank
        @Size(max = 20)
        String title,

        @NotBlank
        @Size(max = 100)
        String body,

        @NotNull
        Long memberId
) {
}