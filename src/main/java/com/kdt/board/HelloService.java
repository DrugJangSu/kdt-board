package com.kdt.board;

import org.springframework.stereotype.Service;

// 메시지를 제공하는 서비스
@Service
public class HelloService {
    // 고정 문자열 "kimbap" 반환
    public String message() {
        return "kimbap";
    }
}
