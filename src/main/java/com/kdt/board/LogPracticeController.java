package com.kdt.board;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 로그 레벨별 출력 동작을 확인하는 연습용 컨트롤러
@RestController
public class LogPracticeController {
    // SLF4J 로거: 클래스 이름이 로그에 함께 찍힙니다.
    private static final Logger log = LoggerFactory.getLogger(LogPracticeController.class);


    // /logs/check: println, debug, info 로그를 한 번씩 남기고 "logged"를 반환합니다.
    @GetMapping("/logs/check")
    public String check() {
        System.out.println("log practice println");
        log.debug("log practice debug");
        log.info("log practice info");
        return "logged";
    }

}

