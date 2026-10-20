package com.kdt.board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 게시글 조회 컨트롤러
@RestController
public class PostController {

    // SLF4J 로거: 클래스 이름이 로그에 함께 찍힙니다.
    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // /posts: 게시글마다 ID·제목·생성 시각을 한 줄로 구성합니다.
    @GetMapping("/posts")
    public String posts() {
        StringBuilder lines = new StringBuilder();
        for (Post post : this.postService.findAll()) {
            lines.append(post.getId())
                    .append(" ")
                    .append(post.getTitle())
                    .append(" ")
                    .append(post.getCreatedAt())
                    .append(" ")
                    .append(post.getUpdatedAt())
                    .append("\n");
        }
        return lines.toString();
    }

    // 경로의 1과 findById의 1L을 확인한 실제 closed ID로 맞춥니다.
    @GetMapping("/posts/1")
    public String onePost() {
        Post post = this.postService.findById(1L);
        if (post == null) {
            return "missing";
        }
        // 작성자 객체에서 loginId를 읽어 제목과 함께 응답합니다.
        return post.getTitle()
                + " by "
                + post.getMember().getLoginId();
    }

    @GetMapping("/posts/with-member")
    public String withMember() {
        log.info("sql compare normal begin");
        int rows = 0;
        StringBuilder lines = new StringBuilder();
        for (Post post : this.postService.findAll()) {
            lines.append(post.getTitle())
                    .append(" by ")
                    .append(post.getMember().getLoginId())
                    .append("\n");
            rows++;
        }
        log.info("sql compare normal end rows = {}", rows);
        return lines.toString();
    }

    @GetMapping("/posts/joined")
    public String joined(){
        log.info("sql compare joined begin");
        int rows = 0;
        StringBuilder lines = new StringBuilder();
        for (Post post : this.postService.findAllWithMember()) {
            lines.append(post.getTitle()).append(" by ")
                    .append(post.getMember().getLoginId()).append("\n");
            rows++;
        }
        log.info("sql compare joined end rows={}", rows);
        return lines.toString();
    }

    @GetMapping("/posts/count")
    public String count() {
        return "" + this.postService.countPosts();
    }


    @GetMapping("/posts/list-count")
    public String listCount() {
        return "" + this.postService.countLoadedPosts();
    }

}
