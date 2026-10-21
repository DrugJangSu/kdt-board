package com.kdt.board;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

// AOP 관점(Aspect) 클래스임을 선언
@Aspect
// 스프링 빈으로 등록
@Component
// Lombok이 log 필드(SLF4J 로거)를 자동 생성
@Slf4j
public class TxOutcomeAspect {

    // 대상 메서드가 정상 종료된 뒤에 실행
    @AfterReturning(
            // BoardTxService의 registerOk 또는 registerFail 메서드(인자 무관)를 대상으로 지정
            pointcut = "execution(* com.kdt.board.BoardTxService.registerOk(..)) || " +
                    "execution(* com.kdt.board.BoardTxService.registerFail(..))",
            // 대상 메서드의 반환값을 seen 파라미터로 받음
            returning = "seen"
    )
    public void returned(JoinPoint joinPoint, Object seen) {
        // 호출된 메서드 이름과 반환값을 로그로 남김
        log.info("outcome returned " + joinPoint.getSignature().getName() + " " + seen);

    }

    // 대상 메서드에서 예외가 던져진 뒤에 실행
    @AfterThrowing(
            // 위와 동일하게 registerOk / registerFail 메서드를 대상으로 지정
            pointcut = "execution(* com.kdt.board.BoardTxService.registerOk(..)) || " +
                    "execution(* com.kdt.board.BoardTxService.registerFail(..))",
            // 던져진 예외를 ex 파라미터로 받음 (IllegalStateException일 때만 매칭)
            throwing = "ex"
    )
    public void failed(JoinPoint joinPoint, IllegalStateException ex) {
        // 호출된 메서드 이름과 예외 메시지를 로그로 남김
        log.info("outcome threw " + joinPoint.getSignature().getName() + " " + ex.getMessage());
    }

    // 대상 메서드가 정상 종료되든 예외가 나든 항상 마지막에 실행 (finally와 유사)
    @After("execution(* com.kdt.board.BoardTxService.registerOk(..)) || " +
            // registerOk 또는 registerFail 메서드(인자 무관)를 대상으로 지정
            "execution(* com.kdt.board.BoardTxService.registerFail(..))")
    public void finished(JoinPoint joinPoint) {
        // 호출된 메서드 이름과 함께 종료 사실을 로그로 남김
        log.info("outcome finished " + joinPoint.getSignature().getName());
    }

    // @Around: 대상 메서드 실행 "전과 후"를 모두 감싸는 가장 강력한 어드바이스
    // 대상 메서드를 실행할지, 언제 실행할지를 직접 제어할 수 있음
    @Around("execution(* com.kdt.board.BoardTxService.registerFail(..))")
    // ProceedingJoinPoint: JoinPoint에 proceed()(실제 메서드 실행) 기능이 추가된 객체
    // 반환 타입이 Object인 이유: 대상 메서드의 반환값을 그대로 호출한 쪽에 돌려줘야 하기 때문
    // throws Throwable: proceed()가 대상 메서드의 어떤 예외든 던질 수 있어서 필요
    public Object aroundFail(ProceedingJoinPoint joinPoint) throws Throwable {
        // 시작 시각 기록 (나노초 단위, 나중에 걸린 시간을 계산하기 위함)
        long started = System.nanoTime();
        // 대상 메서드 실행 직전에 찍히는 로그
        log.info("failure around begin");
        try {
            // 여기서 진짜 registerFail 메서드가 실행됨 (이 줄을 호출하지 않으면 대상 메서드는 실행되지 않음)
            Object result = joinPoint.proceed();
            // 예외 없이 정상 종료됐을 때만 찍히는 로그
            log.info("failure around normal end");
            // 대상 메서드의 반환값을 그대로 전달 (빼먹으면 호출한 쪽은 null을 받게 됨)
            return result;
        } finally {
            // finally: 정상 종료든 예외든 무조건 실행되는 블록
            // 현재 시각 - 시작 시각 = 대상 메서드가 걸린 시간(나노초)
            long elapsed =System.nanoTime() - started;
            // 걸린 시간을 로그로 남김
            log.info("failure around finally elapsedNs= " + elapsed);
        }
    }
}
