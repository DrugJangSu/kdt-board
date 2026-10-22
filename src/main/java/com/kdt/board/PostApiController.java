// 이 파일이 속한 폴더(패키지) 이름이에요. 같은 패키지의 PostService, PostResponse는 import 없이 바로 쓸 수 있어요.
package com.kdt.board;

// 스프링이 필요한 객체(PostService)를 자동으로 넣어주게 하는 어노테이션을 가져와요.
import org.springframework.beans.factory.annotation.Autowired;
// "GET 요청이 오면 이 메서드를 실행해라"라고 지정하는 어노테이션을 가져와요.
import org.springframework.web.bind.annotation.*;
// 클래스나 메서드에 URL 주소를 연결해주는 어노테이션을 가져와요.
// 화면(HTML)이 아니라 데이터(JSON)를 응답하는 컨트롤러임을 표시하는 어노테이션을 가져와요.

// 여러 개의 값을 순서대로 담는 목록(List)을 사용하기 위해 가져와요.
import java.util.List;

// 이 클래스는 REST API 컨트롤러예요. 반환값이 자동으로 JSON으로 바뀌어 브라우저/클라이언트에 전달돼요.
@RestController
// 이 컨트롤러의 모든 요청 주소는 "/api/v1/posts"로 시작해요.
@RequestMapping("/api/v1/posts")
// 게시글(Post) 관련 API 요청을 받아 처리하는 클래스예요.
public class PostApiController {
    // 실제 게시글 처리 로직은 PostService가 담당해요. 컨트롤러는 요청을 받아 서비스에 넘기기만 해요.
    // final: 한 번 정해지면 바꿀 수 없어서 안전해요.
    private final PostService postService;

    // 스프링이 이 생성자를 호출하면서 PostService 객체를 알아서 넣어줘요(의존성 주입).
    @Autowired
    // 생성자: PostApiController 객체를 만들 때 PostService를 전달받아요.
    public PostApiController(PostService postService) {
        // 전달받은 postService를 이 클래스의 필드에 저장해서 아래 메서드에서 사용해요.
        this.postService = postService;
    }

    // GET /api/v1/posts 요청이 오면 아래 list() 메서드가 실행돼요.
    @GetMapping()
    // 게시글 목록을 PostResponse 리스트 형태로 돌려줘요. (JSON 배열로 변환되어 응답돼요)
    public List<PostResponse> list() {
        // 서비스에게 게시글 목록 조회를 맡기고, 그 결과를 그대로 반환해요.
        return this.postService.listResponse();
    }
    @GetMapping("/{id}")
    public PostResponse one(@PathVariable("id") Long id) {
        return this.postService.findResponse(id);
    }

    @GetMapping("/count-by-title")
    public long countByTitle(@RequestParam("title") String title) {

        return this.postService.countByTitle(title);
    }
}
