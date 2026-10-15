package com.kdt.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class BoardSeedRunner implements ApplicationRunner {

    private final MemberService memberService;

    @Autowired
    public BoardSeedRunner(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (Member member : this.memberService.findAll()) {
            if ("kdt01".equals(member.getLoginId())) {
                System.out.println("seed skip kdt01");
                return;
            }
        }
        Member member = new Member();
        member.setLoginId("kdt01");
        member.setNickname("kimbap");
        System.out.println("id before save = " + member.getId());
        Member saved = this.memberService.save(member);
        System.out.println("id after save = " + saved.getId());
        System.out.println("seed member kdt01");


    }
}