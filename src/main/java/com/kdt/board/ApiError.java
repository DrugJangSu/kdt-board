package com.kdt.board;

// 에러가 났을 때 클라이언트에게 돌려줄 JSON 응답의 모양(에러 코드 + 설명 메시지)이에요.
public record ApiError(String code, String message) {
}