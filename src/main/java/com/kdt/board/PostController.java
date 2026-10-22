package com.kdt.board;

// 요청 데이터가 규칙(@NotBlank, @Size 등)에 맞는지 검사하게 하는 어노테이션이에요.
import jakarta.validation.Valid;
// 로그를 남기기 위한 SLF4J의 Logger 클래스와 만들어주는 공장(LoggerFactory)이에요.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// HTTP 상태코드·헤더·본문을 직접 정해서 응답할 때 쓰는 클래스예요.
import org.springframework.http.ResponseEntity;
// GET 요청(조회)과 POST 요청(생성)을 메서드에 연결하는 어노테이션이에요.
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// 요청 본문(JSON)을 자바 객체로 바꿔 받게 하는 어노테이션이에요.
import org.springframework.web.bind.annotation.RequestBody;
// 화면(HTML)이 아니라 데이터를 응답하는 컨트롤러임을 표시하는 어노테이션이에요.
import org.springframework.web.bind.annotation.RestController;

// 새로 만든 게시글의 주소(Location)를 표현하는 URI 클래스예요.
import java.net.URI;

// 게시글 조회 컨트롤러 (화면용 간단한 문자열 응답과, 쿼리 수 비교 실습용 엔드포인트가 모여 있어요)
@RestController
public class PostController {

    // SLF4J 로거: 클래스 이름이 로그에 함께 찍힙니다.
    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    // 게시글 처리 로직을 담당하는 서비스예요. 컨트롤러는 요청을 받아 서비스에 넘겨요.
    private final PostService postService;

    // 스프링이 PostService를 자동으로 넣어주는 생성자예요(의존성 주입).
    public PostController(PostService postService) {
        // 전달받은 서비스를 필드에 저장해 아래 메서드들에서 사용해요.
        this.postService = postService;
    }

    // /posts: 게시글마다 ID·제목·생성 시각을 한 줄로 구성합니다.
    @GetMapping("/posts")
    public String posts() {
        // 여러 줄의 문자열을 효율적으로 이어 붙이기 위한 도구예요.
        StringBuilder lines = new StringBuilder();
        // 전체 게시글을 하나씩 꺼내 한 줄씩 만들어요. (id 제목 생성시각 수정시각 순서)
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
        // 모아 둔 줄들을 하나의 문자열로 바꿔 응답해요.
        return lines.toString();
    }

    // 경로의 1과 findById의 1L을 확인한 실제 closed ID로 맞춥니다.
    @GetMapping("/posts/1")
    public String onePost() {
        // id가 1인 게시글을 조회해요. (1L의 L은 long 타입 숫자라는 표시예요)
        Post post = this.postService.findById(1L);
        // 게시글이 없으면(null) 에러 대신 "missing" 문자열을 돌려줘요.
        if (post == null) {
            return "missing";
        }
        // 작성자 객체에서 loginId를 읽어 제목과 함께 응답합니다.
        return post.getTitle()
                + " by "
                + post.getMember().getLoginId();
    }

    // /posts/with-member: 일반 findAll 후 작성자를 접근해 N+1 쿼리 발생을 비교합니다.
    @GetMapping("/posts/with-member")
    public String withMember() {
        // 실행한 SQL이 이 로그 사이에 찍히므로, begin/end 로그로 구간을 구분해요.
        log.info("sql compare normal begin");
        // 처리한 게시글 수를 세는 변수예요.
        int rows = 0;
        StringBuilder lines = new StringBuilder();
        // findAll은 게시글만 가져오므로, 아래에서 작성자(getMember)에 접근할 때마다 추가 쿼리가 나갈 수 있어요(N+1).
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

    // /posts/joined: join fetch로 게시글과 작성자를 한 번에 조회해 쿼리 수를 비교합니다.
    @GetMapping("/posts/joined")
    public String joined(){
        // 이 로그와 end 로그 사이에 나간 SQL 개수를 위의 withMember와 비교해요.
        log.info("sql compare joined begin");
        int rows = 0;
        StringBuilder lines = new StringBuilder();
        // 게시글과 작성자를 join으로 한 번에 가져오므로 getMember 접근 시 추가 쿼리가 나가지 않아요.
        for (Post post : this.postService.findAllWithMember()) {
            lines.append(post.getTitle()).append(" by ")
                    .append(post.getMember().getLoginId()).append("\n");
            rows++;
        }
        log.info("sql compare joined end rows={}", rows);
        return lines.toString();
    }

    // /posts/count: DB count 쿼리로 게시글 수를 조회합니다.
    @GetMapping("/posts/count")
    public String count() {
        // 숫자 앞에 ""를 붙여 문자열로 바꿔 응답해요. (DB가 직접 개수만 세서 가벼워요)
        return "" + this.postService.countPosts();
    }


    // /posts/list-count: 전체 게시글을 로딩한 뒤 리스트 크기로 개수를 셉니다.
    @GetMapping("/posts/list-count")
    public String listCount() {
        // 게시글을 전부 메모리로 읽은 뒤 크기를 세므로, /posts/count보다 무거운 방식이에요.
        return "" + this.postService.countLoadedPosts();
    }

    // /posts/title-count/closed: 제목이 "closed"인 게시글 수를 반환합니다.
    @GetMapping("/posts/title-count/closed")
    public String countClosed() {
        // 제목을 "closed"로 고정해 서비스에 개수 조회를 맡기고, 결과를 문자열로 응답해요.
        return "" + this.postService.countByTitle("closed");
    }

    // POST 요청으로 게시글을 새로 만들어요. 요청 JSON은 @Valid로 제목·본문·회원id 검증을 거쳐요.
    @PostMapping()
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostCreateRequest request) {
        // 서비스에게 게시글 생성을 맡기고, 만들어진 결과를 PostResponse로 받아요.
        PostResponse response = this.postService.create(request);
        // 새로 만든 게시글을 조회할 수 있는 주소(Location 헤더에 담길 값)를 만들어요.
        URI location = URI.create("/api/v1/posts/" + response.id());
        // HTTP 201(생성됨) 상태코드와 Location 헤더, 응답 본문을 함께 돌려줘요.
        return ResponseEntity.created(location).body(response);
    }
}

