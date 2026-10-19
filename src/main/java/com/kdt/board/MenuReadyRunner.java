//package com.kdt.board;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MenuReadyRunner implements ApplicationRunner {
//
//    private final HelloService helloService;
//
//    @Autowired
//    public MenuReadyRunner(HelloService helloService) {
//        this.helloService = helloService;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        System.out.println("menu ready " + this.helloService.message());
//    }
//}