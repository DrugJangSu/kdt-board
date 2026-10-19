package com.kdt.board;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 공지 확인용 컨트롤러
@RestController
public class NoticePingController {

    // /notice -> "closed" 응답
    @GetMapping("/notice")
    public String notice() {
        return "closed";
    }

}
