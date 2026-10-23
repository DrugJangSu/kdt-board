package com.kdt.board;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// content() : 응답 본문(body)을 검사할 때 쓰는 도구
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// status() : 응답 상태 코드(200, 404 등)를 검사할 때 쓰는 도구
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * HelloController 웹 계층 테스트 (Slice Test)
 *
 * @WebMvcTest
 *  - 스프링 전체를 띄우지 않고, "웹(컨트롤러) 부분만" 가볍게 띄워서 테스트합니다.
 *  - Service, Repository 같은 다른 객체는 자동으로 만들어주지 않습니다.
 *    그래서 아래처럼 가짜(Mock)를 직접 넣어줘야 합니다.
 */
@WebMvcTest(HelloControllerTest.class)
public class HelloControllerTest {

    // MockMvc : 진짜 서버를 켜지 않고도 "GET /hello 요청을 보낸 것처럼" 흉내 내주는 도구입니다.
    // @Autowired : 스프링이 만들어둔 MockMvc 객체를 자동으로 넣어달라는 뜻입니다.
    @Autowired
    private MockMvc mockMvc;

    // @MockitoBean : HelloService의 "가짜 버전"을 만들어 스프링에 등록합니다.
    //   컨트롤러는 HelloService가 필요한데, 이 테스트에서는 컨트롤러만 검사하고 싶으니
    //   진짜 서비스 대신 내가 원하는 대로 움직이는 가짜를 쓰는 것입니다.
    @MockitoBean
    private HelloService helloService;

    @Test
    void hello_returns_kimbap() throws Exception {
        //arrange(준비)
        // given(...).willReturn(...) : "가짜 서비스의 message()가 호출되면 "kimbap"을 돌려줘!" 라고 미리 정해두는 것.
        // 이렇게 가짜의 행동을 정해두는 것을 스텁(stub, stubbing)이라고 합니다.
        given(this.helloService.message()).willReturn("kimbap"); // 스텁(stub, stubbing)

        //act(실행)
        // mockMvc.perform(...) : 가짜 HTTP 요청을 컨트롤러에 보냅니다. (실제 서버는 켜지 않음)
        // get("/hello")        : "GET /hello" 요청을 만든다는 뜻입니다. (브라우저 주소창에 /hello 입력하는 것과 같음)
        this.mockMvc.perform(get("/hello"))
                // assert(검증)
                // andExpect(...) : 응답이 내가 기대한 것과 맞는지 확인합니다. 하나라도 다르면 테스트 실패!
                // status().isOk() : HTTP 상태 코드가 200(성공)인지 확인합니다.
                .andExpect(status().isOk())
                // content().string("kimbap") : 응답 본문(body)이 정확히 "kimbap" 문자열인지 확인합니다.
                .andExpect(content().string("kimbap"));
    }
}
