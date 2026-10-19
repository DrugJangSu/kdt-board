package com.kdt.board;

import org.springframework.data.jpa.repository.JpaRepository;

// 게시글 DB 접근 인터페이스 (기본 CRUD 제공)
public interface PostRepository extends JpaRepository<Post, Long> {

}
