package com.kdt.board;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

// HelloService.message2() 실행 앞뒤에 로그를 끼워 넣는 AOP 예제 클래스입니다.
// message2() 코드는 고치지 않고, 이 클래스가 바깥에서 "앞뒤로 감싸는" 구조입니다.
@Aspect      // "이 클래스는 AOP 기능을 담고 있다"고 스프링에게 알려줍니다.
@Component   // 스프링이 객체로 만들어 관리(빈 등록)해야 AOP가 실제로 동작합니다. 빠지면 무시됩니다.
@Slf4j       // 롬복이 log 객체를 자동으로 만들어 줍니다. System.out.println 대신 log.info()로 로그를 남깁니다.
public class HelloMessageAspect {

    // @Around : 대상 메서드를 통째로 감싸서, 실행 전/후에 원하는 코드를 넣을 수 있습니다.
    // execution(...) : 대상 메서드를 지정하는 표현식입니다.
    //   * (맨 앞) -> 반환 타입은 무엇이든 OK
    //   HelloService.message2(String,String) -> 파라미터가 String 2개인 message2 메서드만 대상
    // ProceedingJoinPoint : 가로챈 메서드를 "내가 원할 때 실행"시킬 수 있는 객체
    // throws Throwable    : 원래 메서드에서 예외가 나면 그대로 밖으로 던지기 위해 필요
    @Around("execution(* com.kdt.board.HelloService.message2(java.lang.String,java.lang.String))")
    public Object aroundMessage2(ProceedingJoinPoint joinPoint) throws Throwable {

        // 1) 원래 메서드 실행 "전"에 할 일: 메서드 이름("message2")을 로그로 남깁니다.
        log.info("AOP 시작: " + joinPoint.getSignature().getName());

        // 2) 원래 메서드(message2)를 실제로 실행하고, 반환값을 받아 둡니다.
        //    주의: proceed()를 호출하지 않으면 원래 메서드는 아예 실행되지 않습니다!
        Object proceed = joinPoint.proceed();

        // 3) 원래 메서드 실행 "후"에 할 일
        log.info("aop end");

        // 4) 원래 메서드의 반환값을 그대로 돌려줍니다.
        //    빼먹으면 호출한 쪽(컨트롤러)이 null 등 엉뚱한 값을 받게 됩니다.
        return proceed;

    }
}
