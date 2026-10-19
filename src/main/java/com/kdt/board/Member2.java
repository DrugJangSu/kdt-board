package com.kdt.board;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

// 다른 엔티티에 포함해서 쓰는 값 타입 (id 두 개로 구성)
@Embeddable
public class Member2 implements Serializable {
    private Long id;
    private Long id2;
}
