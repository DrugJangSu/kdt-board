package com.kdt.board;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 요금 조회 컨트롤러
@RestController
@RequiredArgsConstructor
public class FareController {
    private final FareService fareService;

    // /fare -> FareService의 요금 응답
    @GetMapping("/fare")
    public String fare() {
        return fareService.price();
    }

}
