package com.kdt.board;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TxOutcomeAspect {

    @AfterReturning(
            pointcut = "execution(* com.kdt.board.BoardTxService.registerOk(..)) || " +
                    "execution(* com.kdt.board.BoardTxService.registerFail(..))",
            returning = "seen"
    )
    public void returned(JoinPoint joinPoint, Object seen) {
        log.info("outcome returned " + joinPoint.getSignature().getName() + " " + seen);
    }
}
