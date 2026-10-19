//package com.kdt.board;
//
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class BoardSeedRunner implements ApplicationRunner {
//
//    private final MemberService memberService;
//    private final PostService postService;
//
//    public BoardSeedRunner(MemberService memberService, PostService postService) {
//        this.memberService = memberService;
//        this.postService = postService;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) {
//        // 기존 회원을 조회하고 없는 경우에만 저장합니다.
//        Member saved = null;
//        for (Member member : this.memberService.findAll()) {
//            if ("kdt01".equals(member.getLoginId())) {
//                saved = member;
//                break;
//            }
//        }
//        if (saved == null) {
//            Member member = new Member();
//            member.setLoginId("kdt01");
//            member.setNickname("kimbap");
//            saved = this.memberService.save(member);
//            System.out.println("seed member kdt01");
//        } else {
//            System.out.println("seed skip kdt01");
//        }
//
//        // kdt01이 작성한 closed와 opened가 있는지 각각 확인합니다.
//        boolean hasClosed = false;
//        boolean hasOpened = false;
//        for (Post post : this.postService.findAll()) {
//            if ("closed".equals(post.getTitle())
//                    && saved.getId().equals(post.getMember().getId())) {
//                hasClosed = true;
//            }
//            if ("opened".equals(post.getTitle())
//                    && saved.getId().equals(post.getMember().getId())) {
//                hasOpened = true;
//            }
//        }
//        if (!hasClosed) {
//            Post closed = new Post();
//            closed.setTitle("closed");
//            closed.setBody("no class");
//            closed.setMember(saved);
//            this.postService.save(closed);
//            System.out.println("seed post closed");
//        } else {
//            System.out.println("seed skip closed");
//        }
//        if (!hasOpened) {
//            Post opened = new Post();
//            opened.setTitle("opened");
//            opened.setBody("after column");
//            opened.setMember(saved);
//            System.out.println("opened id before save=" + opened.getId());
//            System.out.println("opened createdAt before save=" + opened.getCreatedAt());
//            Post savedOpened = this.postService.save(opened);
//            System.out.println("opened id after save=" + savedOpened.getId());
//            System.out.println("opened createdAt after save=" + savedOpened.getCreatedAt());
//            System.out.println("seed post opened");
//        } else {
//            System.out.println("seed skip opened");
//        }
//    }
//}