package com.kdt.board;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * HelloService 단위 테스트 (Unit Test)
 *
 * 단위 테스트란?
 *  - 스프링을 띄우지 않고, 클래스 하나만 "순수 자바"로 만들어서 빠르게 검사하는 테스트입니다.
 *  - 그래서 속도가 매우 빠르고, 실패했을 때 원인을 찾기 쉽습니다.
 */
public class HelloServiceTest {


    // @Test : "이 메서드는 테스트입니다"라고 JUnit에게 알려주는 표시입니다.
    //         (메서드 이름은 "무엇을 하면 -> 어떤 결과가 나온다"처럼 읽히게 짓는 것이 좋습니다)
    @Test
    void message_returns_kimbap(){
        // Arrange(준비)
        // 테스트할 대상을 직접 new 로 만듭니다. (스프링이 만들어주는 게 아니라 내가 직접 생성)
        HelloService helloService = new HelloService();

        // Act(실행) + Assert(검증)
        // helloService.message() -> 실제로 메서드를 실행해서 결과를 얻습니다. (Act)
        // assertEquals(기대값, 실제값) -> 두 값이 같은지 비교합니다. (Assert)
        //   같으면 테스트 통과, 다르면 테스트 실패!
        // 이렇게 "준비 -> 실행 -> 검증" 순서로 쓰는 방식을 AAA 패턴이라고 합니다.
        assertEquals("kimbap",helloService.message());
    }
}
