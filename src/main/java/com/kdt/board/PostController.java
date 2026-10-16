package com.kdt.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // /posts: 글마다 "번호 제목" 한 줄을 모읍니다
    @GetMapping("/posts")
    public String posts() {
        StringBuilder lines = new StringBuilder();
        for (Post post : this.postService.findAll()) {
            lines.append(post.getId())
                    .append(" ")
                    .append(post.getTitle())
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

}