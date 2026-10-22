// 이 파일이 속한 폴더(패키지) 이름이에요. 같은 패키지의 PostService, PostResponse는 import 없이 바로 쓸 수 있어요.
package com.kdt.board;

// 요청 데이터(@RequestBody)가 규칙(@NotBlank, @Size 등)에 맞는지 검사하게 하는 어노테이션을 가져와요.
import jakarta.validation.Valid;
// 스프링이 필요한 객체(PostService)를 자동으로 넣어주게 하는 어노테이션을 가져와요.
import org.springframework.beans.factory.annotation.Autowired;
// HTTP 상태코드·헤더·본문을 직접 정해서 응답할 때 쓰는 클래스를 가져와요.
import org.springframework.http.ResponseEntity;
// @RestController, @GetMapping, @PostMapping, @PutMapping, @PathVariable, @RequestParam, @RequestBody 등 웹 요청용 어노테이션을 한꺼번에 가져와요.
import org.springframework.web.bind.annotation.*;

// 새로 만든 게시글의 주소(Location)를 표현하는 URI 클래스를 가져와요.
import java.net.URI;
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

    // GET /api/v1/posts/3 처럼 주소 끝의 숫자(id)로 게시글 한 개를 조회해요.
    @GetMapping("/{id}")
    // 주소의 {id} 자리 값을 꺼내(@PathVariable) id 변수에 넣어줘요.
    public PostResponse one(@PathVariable("id") Long id) {
        // 서비스에서 id에 해당하는 게시글을 찾아 PostResponse로 반환해요. (없으면 NotFoundException 발생)
        return this.postService.findResponse(id);
    }

    // GET /api/v1/posts/count-by-title?title=안녕 처럼 제목이 같은 게시글 개수를 세요.
    @GetMapping("/count-by-title")
    // 주소의 ?title=값 부분(쿼리 파라미터)을 title 변수로 받아요.
    public long countByTitle(@RequestParam("title") String title) {
        // 서비스에게 해당 제목의 게시글 개수를 세도록 맡기고 결과(숫자)를 반환해요.
        return this.postService.countByTitle(title);
    }

    // POST /api/v1/posts
// JSON으로 받은 내용을 이용해 새 게시글을 생성
    @PostMapping
    public ResponseEntity<PostResponse> create(
            @Valid @RequestBody PostCreateRequest request
    ) {
        // 서비스에게 실제 게시글 저장을 맡김
        PostResponse response = this.postService.create(request);

        // 방금 생성된 게시글의 주소를 만듦
        // 예: 게시글 id가 7이면 /api/v1/posts/7
        URI location = URI.create("/api/v1/posts/" + response.id());

        // 201 Created + Location 헤더 + 생성된 게시글 정보 반환
        return ResponseEntity.created(location).body(response);
    }

    // PUT /api/v1/posts/3 처럼 id로 지정한 게시글의 내용을 수정해요. (POST는 새로 만들기, PUT은 기존 것 고치기)
    @PutMapping("/{id}")
    public PostResponse update(
            // 주소의 {id} 값이 수정할 게시글 번호예요.
            @PathVariable("id") Long id,
            // 요청 본문(JSON)을 PostUpdateRequest로 바꾸고, @Valid로 입력 규칙을 검사해요.
            @Valid @RequestBody PostUpdateRequest request) {
        // 서비스에게 수정을 맡기고, 수정된 게시글을 PostResponse로 돌려받아요. (없는 id면 NotFoundException → 404)
        return this.postService.update(id, request);
    }

    // DELETE /api/v1/posts/3 처럼 id로 지정한 게시글을 삭제해요. (성공하면 내용 없이 204 응답)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
