package com.kdt.board;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/testhello")
    public String hello2() {
        List<String> strings = this.helloService.message2("good", "h2");

        String ab = strings.get(0) + strings.get(1);
        TitleResponse good = new TitleResponse("good");
        good.title();

        return ab;
    }
}