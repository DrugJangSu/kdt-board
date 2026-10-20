package com.kdt.board;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * AOP(관점 지향 프로그래밍) 예제 클래스.
 *
 * AOP란? 여러 곳에서 공통으로 필요한 기능(로그 출력, 시간 측정, 트랜잭션 등)을
 * 원래 코드를 고치지 않고 "끼워 넣을 수 있게" 해주는 방법입니다.
 *
 * 이 클래스는 BoardTxService.registerOk() 메서드가 실행되기 직전에
 * 콘솔에 메서드 이름을 찍어서, 해당 메서드가 호출되는지 확인(probe = 탐침)하는 용도입니다.
 */
@Aspect      // "이 클래스는 AOP 기능을 담고 있다"고 스프링에게 알려줍니다.
@Component   // 스프링이 이 클래스를 자동으로 객체로 만들어 관리(빈 등록)하게 합니다.
public class TxProbeAspect {

    // @Before : 대상 메서드가 실행되기 "전에" 아래 메서드를 먼저 실행합니다.
    // execution(...) : 어떤 메서드를 대상으로 할지 정하는 표현식입니다.
    //   * (맨 앞)                              -> 반환 타입은 무엇이든 OK
    //   com.kdt.board.BoardTxService.registerOk -> 이 클래스의 registerOk 메서드
    //   (..)                                   -> 파라미터는 몇 개든, 어떤 타입이든 OK
    @Before("execution(* com.kdt.board.BoardTxService.registerOk(..))")
    public void beforeOk(JoinPoint joinPoint) {
        // JoinPoint : 지금 가로챈 메서드에 대한 정보(이름, 파라미터 등)를 담은 객체
        // getSignature().getName() : 가로챈 메서드의 이름 ("registerOk")
        System.out.println("aspect " + joinPoint.getSignature().getName());
    }
}
