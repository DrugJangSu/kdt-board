package com.kdt.board;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

// HelloService.message2() 실행 전/후에 로그를 끼워 넣는 AOP 예제 클래스입니다.
// message2() 코드는 전혀 고치지 않고, 이 클래스가 바깥에서 "앞뒤로 끼어드는" 구조입니다.
@Aspect      // "이 클래스는 AOP 기능을 담고 있다"고 스프링에게 알려줍니다.
@Component   // 스프링이 객체로 만들어 관리(빈 등록)해야 AOP가 실제로 동작합니다. 빠지면 무시됩니다.
public class HelloMessageAspect {

    // @Before : message2()가 실행되기 "전에" 이 메서드가 먼저 실행됩니다.
    // execution(...) : 대상 메서드를 지정하는 표현식 ((..) = 파라미터는 뭐든 OK)
    @Before("execution(* com.kdt.board.HelloService.message2(..))")
    public void beforeMessage2(JoinPoint joinPoint) {
        // joinPoint : 가로챈 메서드의 정보(이름 등)를 담은 객체
        System.out.println("AOP 실행 전 :" + joinPoint.getSignature().getName());
    }

    // @After : message2()가 "끝난 뒤에" 실행됩니다. (예외가 나도 실행됨)
    // 실행 순서: "AOP 실행 전" 출력 -> message2() 실행 -> "AOP 실행 후" 출력
    // 파라미터에 JoinPoint 가 없는 이유: 이름 등 메서드 정보가 필요 없어서 생략한 것입니다.
    @After("execution(* com.kdt.board.HelloService.message2(..))")
    public void afterMessage2() {
        System.out.println("AOP 실행 후 : aop end");
    }

}
