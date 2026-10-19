//package com.kdt.board;
//
//import java.time.Instant;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PostAuditUpdateRunner implements ApplicationRunner {
//
//    private final PostService postService;
//
//    public PostAuditUpdateRunner(PostService postService) {
//        this.postService = postService;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) {
//        Long postId = 1L; // kdt01이 작성한 opened의 실제 ID로 변경합니다.
//        String newBody = "after update";
//
//        Post post = this.postService.findById(postId);
//        if (post == null) {
//            System.out.println("audit update: missing");
//            return;
//        }
//        if (!"opened".equals(post.getTitle())) {
//            System.out.println("audit update: not opened, check ID");
//            return;
//        }
//        if (newBody.equals(post.getBody())) {
//            System.out.println("audit update: skip same body");
//            return;
//        }
//
//        Instant beforeCreatedAt = post.getCreatedAt();
//        Instant beforeUpdatedAt = post.getUpdatedAt();
//        System.out.println("audit before id=" + post.getId());
//        System.out.println("audit before body=" + post.getBody());
//        System.out.println("audit before createdAt=" + beforeCreatedAt);
//        System.out.println("audit before updatedAt=" + beforeUpdatedAt);
//
//        post.setBody(newBody);
//        System.out.println("audit after setter updatedAt=" + post.getUpdatedAt());
//        Post savedPost = this.postService.save(post);
//        System.out.println("audit after save body=" + savedPost.getBody());
//        System.out.println("audit after save createdAt=" + savedPost.getCreatedAt());
//        System.out.println("audit after save updatedAt=" + savedPost.getUpdatedAt());
//
//        Post reloaded = this.postService.findById(postId);
//        if (reloaded == null) {
//            System.out.println("audit reload: missing");
//            return;
//        }
//        System.out.println("audit reload body=" + reloaded.getBody());
//        System.out.println("audit reload createdAt=" + reloaded.getCreatedAt());
//        System.out.println("audit reload updatedAt=" + reloaded.getUpdatedAt());
//    }
//}