package com.kdt.board;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class Member2 implements Serializable {
    private Long id;
    private Long id2;
}
