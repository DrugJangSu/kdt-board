package com.kdt.board;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 인사 메시지 컨트롤러
@RestController
@RequiredArgsConstructor
public class HelloController {

    private final HelloService helloService;

    // /hello -> HelloService의 메시지 응답
    @GetMapping("/hello")
    public String hello() {
        return this.helloService.message();
    }
}