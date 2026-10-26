package com.kdt.board;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(PostApiController.class)
@Import(ApiExceptionAdvice.class)
public class PostApiControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;


    // =========================================================
    // 1. 게시글 1건 정상 조회
    // GET /api/v1/posts/1
    // =========================================================
    @Test
    void one_returns_200_json() throws Exception {

        // 준비 : 서비스가 돌려줄 "가짜 응답 데이터"를 만듭니다. (끝의 L 은 Long 타입 숫자라는 표시)
        PostResponse row = new PostResponse(
                1L,
                "closed",
                "kdt01",
                Instant.parse("2026-10-23T00:00:00Z")
        );

        // 스텁(stub) : "서비스의 findResponse(1)이 호출되면 위에서 만든 row 를 돌려줘" 라고 미리 약속해 둡니다.
        given(this.postService.findResponse(1L))
                .willReturn(row);

        // 실행 + 검증
        // get("/api/v1/posts/{id}", 1L) : {id} 자리에 1이 들어가 GET /api/v1/posts/1 요청이 됩니다.
        // status().isOk()               : 상태 코드가 200 인지 확인
        // contentTypeCompatibleWith     : 응답이 JSON 형식인지 확인
        // jsonPath("$.id")              : JSON 에서 id 값을 꺼내 확인 ($ 는 JSON 전체의 시작점)
        this.mockMvc.perform(get("/api/v1/posts/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("closed"))
                .andExpect(jsonPath("$.loginId").value("kdt01"))
                .andExpect(jsonPath("$.createdAt")
                        .value("2026-10-23T00:00:00Z"))
                // length() : JSON 필드 개수. 4개만 나가는지(불필요한 값이 새지 않는지) 확인
                .andExpect(jsonPath("$.length()").value(4));

        // verify : "서비스의 findResponse(1)이 실제로 1번 호출됐는지" 확인합니다.
        verify(this.postService).findResponse(1L);
    }


    // =========================================================
    // 2. 존재하지 않는 게시글 조회
    // GET /api/v1/posts/99999
    // =========================================================
    @Test
    void missing_post_returns_404_json() throws Exception {

        // 스텁 : 이번에는 값을 돌려주는 대신 "없다!"는 예외(NotFoundException)를 던지게 합니다.
        //        ApiExceptionAdvice 가 이 예외를 잡아 404 + 에러 JSON 으로 바꿔줍니다.
        given(this.postService.findResponse(99999L))
                .willThrow(new NotFoundException("post missing"));

        // 실행 + 검증
        this.mockMvc.perform(get("/api/v1/posts/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("post missing"));

        verify(this.postService).findResponse(99999L);
    }


    // =========================================================
    // 3. 제목이 빈 게시글 생성 요청
    // Validation에서 막혀야 하므로 Service가 호출되면 안 됨
    // =========================================================
    @Test
    void blank_title_post_returns_400_before_service() throws Exception {

        // content(""" ... """) : 요청 본문(body)에 담아 보낼 JSON. (""" 는 여러 줄 문자열 문법)
        // title 이 "" 라서 PostCreateRequest 의 검증(@NotBlank)에 걸려 400 이 나와야 합니다.
        this.mockMvc.perform(
                        post("/api/v1/posts")
                                .contentType(APPLICATION_JSON)
                                .content("""
                                        {
                                          "title": "",
                                          "body": "menu",
                                          "memberId": 7
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest());

        // verifyNoInteractions : 서비스가 "한 번도 호출되지 않았음"을 확인합니다.
        //   -> 컨트롤러에 들어오기 전 검증 단계에서 이미 막혔다는 증거입니다.
        verifyNoInteractions(this.postService);
    }


    // =========================================================
    // 4. 게시글 생성 정상 처리
    // POST /api/v1/posts
    // =========================================================
    @Test
    void create_returns_201_and_passes_request_values() throws Exception {

        // 준비 : 클라이언트가 보낸 JSON 이 이 객체와 "같은 값"으로 변환되어 서비스에 전달되길 기대합니다.
        PostCreateRequest expected =
                new PostCreateRequest(
                        "plate",
                        "menu",
                        7L
                );

        PostResponse created =
                new PostResponse(
                        42L,
                        "plate",
                        "writer07",
                        Instant.parse("2026-10-23T00:00:00Z")
                );

        // 스텁 : create(expected) 가 호출되면 created 를 돌려줘.
        //        (record 는 값이 같으면 equals 가 true 라서 JSON 변환 결과와 비교됩니다)
        given(this.postService.create(expected))
                .willReturn(created);

        // 실행 + 검증
        this.mockMvc.perform(
                        post("/api/v1/posts")
                                .contentType(APPLICATION_JSON)
                                .content("""
                                        {
                                          "title": "plate",
                                          "body": "menu",
                                          "memberId": 7
                                        }
                                        """)
                )
                // 201 Created : "새로 만들었다"는 의미의 성공 코드
                .andExpect(status().isCreated())
                // Location 헤더 : 새로 만든 글을 볼 수 있는 주소를 알려줍니다.
                .andExpect(header().string(
                        "Location",
                        "/api/v1/posts/42"
                ))
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.title").value("plate"))
                .andExpect(jsonPath("$.loginId").value("writer07"))
                .andExpect(jsonPath("$.createdAt")
                        .value("2026-10-23T00:00:00Z"))
                .andExpect(jsonPath("$.length()").value(4))
                // doesNotHaveJsonPath : 해당 필드가 응답에 "없어야 한다"는 검증 (요청용 값이 응답에 새지 않는지)
                .andExpect(jsonPath("$.body").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.memberId").doesNotHaveJsonPath());

        verify(this.postService).create(expected);
    }


    // =========================================================
    // 5. 게시글 목록 조회 - 1건 존재
    // GET /api/v1/posts
    // =========================================================
    @Test
    void list_returns_200_json_array() throws Exception {

        // 준비
        List<PostResponse> rows = new ArrayList<>();

        rows.add(
                new PostResponse(
                        1L,
                        "closed",
                        "kdt01",
                        Instant.parse("2026-08-21T00:00:00Z")
                )
        );

        // 스텁
        given(this.postService.listResponse())
                .willReturn(rows);

        // 실행 + 검증
        this.mockMvc.perform(get("/api/v1/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))

                // 응답 전체가 배열인지
                .andExpect(jsonPath("$").isArray())

                // 첫 번째 게시글 검사
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("closed"))
                .andExpect(jsonPath("$[0].loginId").value("kdt01"))
                .andExpect(jsonPath("$[0].createdAt")
                        .value("2026-08-21T00:00:00Z"))

                // 첫 번째 객체는 필드 4개
                .andExpect(jsonPath("$[0].length()").value(4))

                // 배열 전체에는 게시글 1개
                .andExpect(jsonPath("$.length()").value(1));

        verify(this.postService).listResponse();
    }


    // =========================================================
    // 6. 게시글 목록 조회 - 데이터 없음
    // GET /api/v1/posts
    // =========================================================
    @Test
    void empty_list_returns_200_and_empty_array() throws Exception {

        // 스텁
        given(this.postService.listResponse())
                .willReturn(new ArrayList<>());

        // 실행 + 검증
        this.mockMvc.perform(get("/api/v1/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(this.postService).listResponse();
    }
    /*
     * ========================================================================
     * 7) 제목별 개수 조회
     *    GET /api/v1/posts/count-by-title?title=closed  ->  200 + 숫자 2
     * ========================================================================
     *
     * [검증하는 계약]
     *   - 쿼리 파라미터 title이 서비스까지 전달된다.
     *   - 응답이 JSON 숫자(따옴표 없는 2)다.
     *
     * [.param("title", "closed")]
     *   URL 뒤에 ?title=closed를 붙이는 것과 같습니다.
     *
     * [content().string("2")]
     *   primitive long은 Jackson이 문자열 "2"가 아니라 숫자 2로 직렬화합니다.
     *   그래서 본문은 정확히 `2`입니다. jsonPath의 isNumber()와 함께 확인하면
     *   "숫자 타입으로 나갔다"는 점까지 검증됩니다.
     */
    @Test
    void title_query_returns_json_number() throws Exception {
        given(this.postService.countByTitle("closed")).willReturn(2L);

        this.mockMvc.perform(get("/api/v1/posts/count-by-title")
                        .param("title", "closed"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))

                // root 값 자체가 숫자인지 확인
                .andExpect(jsonPath("$").isNumber())

                // 본문 텍스트가 정확히 "2"인지 확인 (따옴표가 없어야 함)
                .andExpect(content().string("2"));

        verify(this.postService).countByTitle("closed");
    }

    /*
     * ========================================================================
     * 8) 제목별 개수 조회 - 필수 파라미터 누락
     *    GET /api/v1/posts/count-by-title  ->  400, 서비스 미호출
     * ========================================================================
     *
     * [검증하는 계약]
     *   @RequestParam("title")은 기본값 required=true입니다.
     *   파라미터가 없으면 스프링이 컨트롤러를 호출하기 전에 400으로 거절합니다.
     */
    @Test
    void missing_title_query_returns_400_before_service() throws Exception {
        this.mockMvc.perform(get("/api/v1/posts/count-by-title"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.postService);
    }

    /*
     * ========================================================================
     * 9) 경로 변수 타입 오류
     *    GET /api/v1/posts/abc  ->  400, 서비스 미호출
     * ========================================================================
     *
     * [검증하는 계약]
     *   컨트롤러의 @PathVariable Long id는 숫자여야 합니다.
     *   "abc"는 Long으로 변환할 수 없으므로 스프링이 400으로 거절합니다.
     *
     * [왜 서비스 미호출을 확인하나?]
     *   타입 변환 단계에서 막혔다는 것을 증명합니다. 잘못된 타입이 서비스까지
     *   내려가면 예상치 못한 예외나 불필요한 DB 조회가 발생할 수 있습니다.
     */
    @Test
    void non_numeric_id_returns_400_before_service() throws Exception {
        this.mockMvc.perform(get("/api/v1/posts/abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.postService);
    }

    /*
     * ========================================================================
     * 10) 수정 성공
     *     PUT /api/v1/posts/1  ->  200 + 수정된 JSON
     * ========================================================================
     *
     * [검증하는 계약]
     *   - PUT 요청 본문이 PostUpdateRequest로 변환된다.
     *   - @Valid 검증을 통과하면 서비스가 호출된다.
     *   - 서비스가 돌려준 수정 결과가 그대로 JSON으로 나간다.
     *
     * [ArgumentMatchers]
     *   given(this.postService.update(eq(1L), any(PostUpdateRequest.class)))
     *   - eq(1L): 값이 1L인 인자와 매칭
     *   - any(PostUpdateRequest.class): 어떤 PostUpdateRequest든 매칭
     *   규칙: 한 메서드 호출에서 인자 하나라도 matcher를 쓰면, 나머지 인자도
     *   모두 matcher(eq/any 등)로 써야 합니다. 섞으면 Mockito가 예외를 던집니다.
     *
     * [verify에서의 record 비교]
     *   new PostUpdateRequest("notice", "edited")는 record이므로 equals가
     *   필드 값으로 비교됩니다. 그래서 값이 정확히 같은지 검증할 수 있습니다.
     */
    @Test
    void update_returns_200_json() throws Exception {
        PostResponse edited = new PostResponse(1L, "notice", "kdt01",
                Instant.parse("2026-08-21T00:00:00Z"));
        given(this.postService.update(eq(1L), any(PostUpdateRequest.class)))
                .willReturn(edited);

        this.mockMvc.perform(put("/api/v1/posts/{id}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\":\"notice\",\"body\":\"edited\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("notice"))
                .andExpect(jsonPath("$.loginId").value("kdt01"))
                .andExpect(jsonPath("$.createdAt").value("2026-08-21T00:00:00Z"))
                .andExpect(jsonPath("$.length()").value(4));

        verify(this.postService).update(1L, new PostUpdateRequest("notice", "edited"));
    }

    /*
     * ========================================================================
     * 11) 수정 실패 - 없는 게시글
     *     PUT /api/v1/posts/999999  ->  404 + 에러 JSON
     * ========================================================================
     *
     * 조회 때와 마찬가지로 서비스가 NotFoundException을 던지면
     * ApiExceptionAdvice가 404 JSON으로 변환합니다.
     */
    @Test
    void missing_post_put_returns_404_json() throws Exception {
        given(this.postService.update(eq(999999L), any(PostUpdateRequest.class)))
                .willThrow(new NotFoundException("post missing"));

        this.mockMvc.perform(put("/api/v1/posts/{id}", 999999L)
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\":\"notice\",\"body\":\"edited\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("post missing"));

        verify(this.postService).update(999999L, new PostUpdateRequest("notice", "edited"));
    }

    /*
     * ========================================================================
     * 12) 수정 실패 - 공백뿐인 제목
     *     PUT /api/v1/posts/1  ->  400, 서비스 미호출
     * ========================================================================
     *
     * [@NotBlank의 의미]
     *   null, "", "   "처럼 공백만 있는 문자열을 모두 거절합니다.
     *   (단순히 길이만 보는 @NotEmpty/@Size와 다른 점입니다.)
     */
    @Test
    void blank_title_put_returns_400_before_service() throws Exception {
        this.mockMvc.perform(put("/api/v1/posts/{id}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\":\"   \",\"body\":\"edited\"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(this.postService);
    }

    /*
     * ========================================================================
     * 13) 삭제 성공
     *     DELETE /api/v1/posts/1  ->  204 No Content
     * ========================================================================
     *
     * [검증하는 계약]
     *   - 상태 코드 204 (성공했지만 돌려줄 본문이 없음)
     *   - 본문이 비어 있음
     *   - 서비스의 delete(1L)가 호출됨
     *
     * [스텁이 없는 이유]
     *   PostService.delete는 void 메서드이고, mock의 void 메서드는 기본적으로
     *   아무 일도 하지 않습니다. 그래서 반환값을 정할 필요가 없습니다.
     */
    @Test
    void delete_ok_returns_204() throws Exception {
        this.mockMvc.perform(delete("/api/v1/posts/{id}", 1L))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(this.postService).delete(1L);
    }

    /*
     * ========================================================================
     * 14) 삭제 실패 - 없는 게시글
     *     DELETE /api/v1/posts/999999  ->  404 + 에러 JSON
     * ========================================================================
     *
     * [void 메서드에 예외 스텁을 거는 BDD 문법]
     *   반환값이 있는 메서드: given(mock.method(...)).willThrow(예외)
     *   반환값이 없는 메서드: willThrow(예외).given(mock).method(...)
     *
     *   void 메서드는 given(...)에 넣을 "반환값"이 없기 때문에 문법이 다릅니다.
     *   이 테스트에서는 delete(999999L)가 호출되면 NotFoundException을 던집니다.
     */
    @Test
    void missing_post_delete_returns_404_json() throws Exception {
        willThrow(new NotFoundException("post missing"))
                .given(this.postService).delete(999999L);

        this.mockMvc.perform(delete("/api/v1/posts/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("post missing"));

        verify(this.postService).delete(999999L);
    }
}