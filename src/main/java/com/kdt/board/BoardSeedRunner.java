package com.kdt.board;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class BoardSeedRunner implements ApplicationRunner {

    private final MemberService memberService;
    private final PostService postService;

    public BoardSeedRunner(MemberService memberService, PostService postService) {
        this.memberService = memberService;
        this.postService = postService;
    }

    @Override
    public void run(ApplicationArguments args) {
        // kdt01 회원이 이미 있으면 그 행을 쓰고 없으면 새로 저장합니다
        Member saved = null;
        for (Member member : this.memberService.findAll()) {
            if ("kdt01".equals(member.getLoginId())) {
                saved = member;
                break;
            }
        }
        if (saved == null) {
            Member member = new Member();
            System.out.println("id before save=" + member.getId());
            member.setLoginId("kdt01");
            member.setNickname("kimbap");
            saved = this.memberService.save(member);
            System.out.println("id after save=" + saved.getId());
            System.out.println("seed member kdt01");
        } else {
            System.out.println("seed skip kdt01");
        }

        // kdt01이 작성한 closed가 있으면 추가 저장하지 않습니다
        for (Post post : this.postService.findAll()) {
            if ("closed".equals(post.getTitle())
                    && saved.getId().equals(post.getMember().getId())) {
                System.out.println("seed skip closed");
                return;
            }
        }
        Post post = new Post();
        post.setTitle("closed");
        post.setBody("no class");
        // 이미 저장된 회원 객체를 작성자로 지정
        post.setMember(saved);
        this.postService.save(post);
        System.out.println("seed post closed");
    }
}