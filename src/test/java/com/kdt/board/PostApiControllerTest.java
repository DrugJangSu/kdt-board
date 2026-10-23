package com.kdt.board;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
 * <p>
 * HelloControllerTest와 같은 방식
 *
 * @WebMvcTest + MockMvc + 가짜 Service(Mock)를 사용합니다.
 */
@WebMvcTest(PostApiController.class)
@Import(ApiExceptionAdvice.class)
public class PostApiControllerTest {

    // 진짜 서버를 실행하지 않고 HTTP 요청을 흉내 내는 도구
    @Autowired
    private MockMvc mockMvc;

    // 진짜 PostService 대신 사용하는 가짜 객체(Mock)
    @MockitoBean
    private PostService postService;

    @Test
    void one_returns_200_json() throws Exception {
        //준비
        PostResponse row = new PostResponse(1L, "closed", "kdt01", Instant.parse("2026-10-23T00:00:00Z"));
        // 스텁
        given(this.postService.findResponse(1L)).willReturn(row);
        //실행
        this.mockMvc.perform(get("/api/v1/posts/{id}", 1L))
                // 검증
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("closed"))
                .andExpect(jsonPath("$.loginId").value("kdt01"))
                .andExpect(jsonPath("$.createdAt").value("2026-10-23T00:00:00Z"))
                .andExpect(jsonPath("$.length()").value(4));

        verify(this.postService).findResponse(1L);
    }
    @Test
    void missing_post_returns_404_json() throws Exception {
        // 준비(arrange)
        // 스텁
        given(this.postService.findResponse(99999L))
                .willThrow(new NotFoundException("post missing"));

        // 실행(act)
        this.mockMvc.perform(get("/api/v1/posts/{id}",99999L))
                //검증(assert)
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("post missing"));

        verify(this.postService).findResponse(99999L);
    }
}
