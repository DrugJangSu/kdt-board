package com.kdt.board;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FareReadyRunner implements ApplicationRunner {
    private final FareService fareService;


    @Override
    public void run (ApplicationArguments args) {
        System.out.println("fare ready " + fareService.price());
    }

}
