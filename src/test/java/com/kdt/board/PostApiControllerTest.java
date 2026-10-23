package com.kdt.board;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * PostApiController 웹 계층 테스트 (게시글 조회 API)
 * HelloControllerTest와 같은 방식(@WebMvcTest + MockMvc + 가짜 Service)입니다.
 */
// @WebMvcTest(PostApiController.class) : 컨트롤러(웹 부분)만 가볍게 띄워서 테스트합니다.
@WebMvcTest(PostApiController.class)
// @Import : @WebMvcTest는 필요한 것만 불러오기 때문에, 예외 처리 클래스(ApiExceptionAdvice)는
//           자동으로 안 들어올 수 있어서 직접 가져옵니다. (예: 글이 없을 때 404 응답을 만들어 주는 역할)
@Import(ApiExceptionAdvice.class)
public class PostApiControllerTest {

    // 진짜 서버 없이 가짜 HTTP 요청을 보내는 도구
    @Autowired
    private MockMvc mockMvc;

    // PostService의 가짜 버전. 컨트롤러만 검사하려고 진짜 서비스(DB 조회 등) 대신 사용합니다.
    @MockitoBean
    private PostService postService;

    @Test
    void one_returns_200_json() throws Exception {
        //준비(arrange)
        // 서비스가 돌려줄 "응답용 게시글 데이터"를 미리 만들어 둡니다.
        // (id=1, 제목="closed", 작성자="kdt01", 작성 시각)
        // Instant : 시간을 나타내는 자바 클래스 (UTC 기준 시각)
        PostResponse row = new PostResponse(1L, "closed", "kdt01", Instant.parse("2026-10-23T00:00:00Z"));
        // 스텁(stub) : "가짜 서비스의 findResponse(1)이 호출되면 위에서 만든 row를 돌려줘!"
        given(this.postService.findResponse(1L)).willReturn(row);

        //실행(act) + 검증(assert)
        // get("/api/v1/posts/{id}", 1L) : GET 요청을 보냅니다. {id} 자리에 1L이 들어갑니다. (→ /api/v1/posts/1)
        this.mockMvc.perform(get("/api/v1/posts/{id}", 1L))
                // 응답 상태 코드가 200(성공)인지 확인
                .andExpect(status().isOk())
                // jsonPath : JSON 응답 안의 값을 꺼내서 검사합니다.
                // "$" 는 JSON 전체(맨 위), "$.title" 은 그 안의 title 항목입니다.
                // 즉 응답 JSON의 title 값이 "closed"인지 확인합니다.
                .andExpect(jsonPath("$.title").value("closed"));
    }
}
