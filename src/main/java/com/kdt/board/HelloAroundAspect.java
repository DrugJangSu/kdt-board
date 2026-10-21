package com.kdt.board;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @Around 어드바이스 예제 클래스.
 *
 * @Before 는 대상 메서드 "실행 전"에만 끼어들 수 있지만,
 * @Around 는 대상 메서드의 "실행 전 + 실행 후"를 모두 감쌀 수 있습니다.
 * (택배 상자를 보내기 전에 포장하고, 도착한 뒤에 확인하는 것과 비슷합니다.)
 *
 * 이 클래스는 HelloService.message() 가 실행되는 앞뒤로 콘솔에 로그를 찍습니다.
 */
@Aspect      // "이 클래스는 AOP 기능을 담고 있다"고 스프링에게 알려줍니다.
@Component   // 스프링이 이 클래스를 자동으로 객체로 만들어 관리(빈 등록)하게 합니다.
public class HelloAroundAspect {

    // @Around : 대상 메서드를 통째로 감싸서, 실행 전/후에 원하는 코드를 넣을 수 있습니다.
    // execution(...) : 어떤 메서드를 대상으로 할지 정하는 표현식입니다.
    //   * (맨 앞)                                -> 반환 타입은 무엇이든 OK
    //   com.kdt.board.HelloService.message       -> 이 클래스의 message 메서드
    //   ()                                       -> 파라미터가 없는 메서드만 대상
    //
    // ProceedingJoinPoint : 가로챈 메서드를 "내가 원할 때 실행"시킬 수 있는 객체입니다.
    // throws Throwable    : 원래 메서드에서 예외가 나면 그대로 밖으로 던지기 위해 필요합니다.
    @Around("execution(* com.kdt.board.HelloService.message())")
    public Object aroundMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1) 원래 메서드 실행 "전"에 할 일
        System.out.println("around begin");

        // 2) 원래 메서드(HelloService.message())를 실제로 실행하고, 반환값을 받아 둡니다.
        //    주의: proceed() 를 호출하지 않으면 원래 메서드는 아예 실행되지 않습니다!
        Object body = joinPoint.proceed();

        // 3) 원래 메서드 실행 "후"에 할 일
        System.out.println("around end");

        // 4) 원래 메서드의 반환값을 그대로 돌려줍니다.
        //    이걸 빼먹거나 null 을 반환하면 호출한 쪽이 엉뚱한 값을 받게 됩니다.
        return body;
    }
}
