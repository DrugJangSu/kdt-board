package com.kdt.board;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 오전/오후 상태를 문자열로 응답하는 컨트롤러
@RestController
public class DayController {

    // /am -> "open" 응답
    @GetMapping("/am")
    public String am() {
        return "open";
    }

    // /pm -> "closed" 응답
    @GetMapping("/pm")
    public String pm() {
        return "closed";
    }
}
