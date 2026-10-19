package com.kdt.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 트랜잭션(롤백) 동작을 URL로 확인해 보는 컨트롤러
@RestController
public class BoardTxController {
    private final BoardTxService boardTxService;

    @Autowired
    public BoardTxController(BoardTxService boardTxService) {
        this.boardTxService = boardTxService;
    }

    // 정상 등록: 회원+게시글이 함께 저장됨
    @GetMapping("/tx/ok")
    public String ok() {
        return this.boardTxService.registerOk();
    }

    // 예외 발생: 트랜잭션이 롤백되어 저장 안 됨
    @GetMapping("/tx/fail")
    public String fail() {
        try {
            return this.boardTxService.registerFail();
        } catch (IllegalStateException ex) {
            return "rolled " + ex.getMessage();
        }
    }

    // 같은 클래스 내부 호출: @Transactional이 적용되지 않아 롤백 안 됨
    @GetMapping("/tx/self")
    public String self() {
        try {
            return this.boardTxService.failBySelf();
        } catch (IllegalStateException ex) {
            return "left " + ex.getMessage();
        }
    }
}