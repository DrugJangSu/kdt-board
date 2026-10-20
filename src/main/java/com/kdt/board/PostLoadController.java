package com.kdt.board;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 지연 로딩(LAZY)이 언제 일어나는지 로그로 확인하는 컨트롤러
@RestController
public class PostLoadController {
    private final PostService postService;

    public PostLoadController(PostService postService) {
        this.postService = postService;
    }

    // /posts/load-check: 게시글 조회 후 작성자 접근 시점을 콘솔에 출력
    @GetMapping("/posts/load-check")
    public String loadCheck() {
        Long postId = 1L; // 확인한 실제 closed의 ID로 변경합니다.
        System.out.println("load: before post lookup");
        Post post = this.postService.findById(postId);
        if (post == null) {
            return "missing";
        }

        System.out.println("load: post loaded, title=" + post.getTitle());
        Member member = post.getMember();
        System.out.println("load: member reference obtained");
        Long memberId = member.getId();
        System.out.println("load: member id=" + memberId);
        System.out.println("load: before loginId");
        String loginId = member.getLoginId();
        System.out.println("load: after loginId=" + loginId);

        return post.getTitle() + " by " + loginId;
    }

    @GetMapping("/posts/read-in-tx")
    public String readInTx() {
        Long postId = 1L;
        return this.postService.findTitleWithLoginId(postId);
    }
}