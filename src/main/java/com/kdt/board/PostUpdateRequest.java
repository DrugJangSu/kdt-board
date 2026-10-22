package com.kdt.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostUpdateRequest(
        @NotBlank
        @Size(max = 20)
        String title,

        @NotBlank
        @Size(max = 100)
        String body
) {
}