package com.kdt.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardTxController {
    private final BoardTxService boardTxService;

    @Autowired
    public BoardTxController(BoardTxService boardTxService) {
        this.boardTxService = boardTxService;
    }

    @GetMapping("/tx/ok")
    public String ok() {
        return this.boardTxService.registerOk();
    }

    @GetMapping("/tx/fail")
    public String fail() {
        try {
            return this.boardTxService.registerFail();
        } catch (IllegalStateException ex) {
            return "rolled " + ex.getMessage();
        }
    }
}