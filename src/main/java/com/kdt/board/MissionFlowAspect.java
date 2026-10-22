// (비활성) HelloService.message()와 BoardTxService.registerFail() 앞뒤로 로그와 걸린 시간을 찍던 AOP 클래스
//package com.kdt.board;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//
//// AOP(관점 지향 프로그래밍)란?
////   여러 곳에서 공통으로 필요한 기능(로그, 시간 측정 등)을 원래 코드를 건드리지 않고
////   "따로 모아서" 끼워 넣는 기술. 이 클래스가 그 "끼워 넣는 코드"를 담고 있음.
//// @Aspect: "이 클래스는 AOP 코드입니다"라고 스프링에게 알려주는 표시
//@Aspect
//// @Component: 스프링이 이 클래스를 직접 만들어서 관리하도록 등록 (이게 없으면 AOP가 동작하지 않음)
//@Component
//public class MissionFlowAspect {
//    // @Around: 대상 메서드가 실행되기 "전과 후"를 모두 감싸는 어드바이스(끼워 넣을 코드)
//    //   -> 마치 택배 상자를 열어서 확인하고 다시 포장해서 보내주는 검문소 같은 역할
//    // 괄호 안의 execution(...)은 "어떤 메서드에 적용할지" 정하는 조건(= pointcut)
//    //   execution(* 패키지.클래스.메서드(..)) 형식이고, * 는 "반환 타입 아무거나", (..) 는 "파라미터 아무거나"라는 뜻
//    // 아래는 HelloService.message() 이거나(||) BoardTxService.registerFail() 일 때 적용한다는 의미
//    @Around("execution(* com.kdt.board.HelloService.message()) || "
//            + "execution(* com.kdt.board.BoardTxService.registerFail(..))")
//    // ProceedingJoinPoint: "지금 가로챈 메서드" 정보를 담고 있고, 그 메서드를 실제로 실행시킬 수 있는 객체
//    // 반환 타입이 Object: 가로챈 메서드가 돌려주는 값을 그대로 다시 돌려줘야 해서 아무 타입이나 받을 수 있게 함
//    // throws Throwable: 가로챈 메서드에서 어떤 예외가 나도 밖으로 그대로 던질 수 있게 하기 위함
//    public Object observe(ProceedingJoinPoint joinPoint) throws Throwable {
//        // 지금 가로챈 메서드의 이름 (예: "message", "registerFail")
//        String name = joinPoint.getSignature().getName();
//        // 시작 시각 저장 (나노초 = 10억분의 1초 단위). 나중에 "얼마나 걸렸는지" 계산하려고 미리 적어둠
//        long started = System.nanoTime();
//        // 메서드가 실행되기 직전에 "시작합니다" 출력
//        System.out.println("mission07 begin " + name);
//        // try / catch / finally: 에러가 나도 정리 작업을 할 수 있게 해주는 구조
//        try {
//            // proceed(): 여기서 진짜 메서드(message 또는 registerFail)가 실행됨
//            // 이 줄을 호출하지 않으면 원래 메서드는 아예 실행되지 않음!
//            Object result = joinPoint.proceed();
//            // 에러 없이 끝났을 때만 실행됨: 메서드 이름과 결과값 출력
//            System.out.println("mission07 returned " + name + " " + result);
//            // 원래 메서드의 결과를 호출한 쪽에 그대로 돌려줌 (안 돌려주면 호출한 쪽은 결과를 못 받음)
//            return result;
//        } catch (IllegalStateException ex) {
//            // 원래 메서드에서 IllegalStateException(잘못된 상태 예외)이 발생했을 때만 여기로 옴
//            // 에러 메시지를 출력해서 "무슨 일이 있었는지" 기록
//            System.out.println("mission07 threw " + name + " " + ex.getMessage());
//            // 예외를 다시 던짐. 그냥 삼켜버리면 호출한 쪽은 에러가 난 줄 모르고 지나가 버리기 때문
//            throw ex;
//        } finally {
//            // finally: 성공하든 실패하든 "무조건 마지막에" 실행되는 블록
//            // 현재 시각 - 시작 시각 = 메서드가 걸린 시간(나노초)
//            long elapsed = System.nanoTime() - started;
//            // 걸린 시간 출력
//            System.out.println("mission07 finally " + name + " elapsedNs=" + elapsed);
//        }
//
//    }
//
//
//}
