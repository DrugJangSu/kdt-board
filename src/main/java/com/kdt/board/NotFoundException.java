package com.kdt.board;

// "찾는 데이터가 없어요"를 알리기 위해 직접 만든 예외예요. (RuntimeException: try/catch 강제 없는 예외)
public class NotFoundException extends RuntimeException {

    // 에러 메시지를 받아서 부모 클래스(RuntimeException)에 넘겨요.
    public NotFoundException(String message) {
        super(message);
    }
}