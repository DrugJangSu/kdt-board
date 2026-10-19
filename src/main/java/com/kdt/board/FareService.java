package com.kdt.board;

import org.springframework.stereotype.Service;

// 요금을 알려주는 서비스
@Service
public class FareService {
    // 고정 요금 "4500" 반환
    public String price() {
        return "4500";
    }
}
