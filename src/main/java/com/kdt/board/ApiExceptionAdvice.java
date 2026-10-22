package com.kdt.board;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 모든 컨트롤러에서 던진 예외를 한곳에서 잡아 JSON 에러 응답으로 바꿔주는 클래스예요.
@RestControllerAdvice
public class ApiExceptionAdvice {

    // NotFoundException이 발생하면 이 메서드가 대신 실행돼요.
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handlerNotFound(NotFoundException ex) {
        // 에러 코드와 예외 메시지를 ApiError에 담아요.
        ApiError body = new ApiError("NOT_FOUND", ex.getMessage());
        // HTTP 상태코드 404(찾을 수 없음)와 함께 에러 내용을 응답해요.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
