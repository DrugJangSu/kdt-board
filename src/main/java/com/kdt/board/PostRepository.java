package com.kdt.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// 게시글 DB 접근 인터페이스 (기본 CRUD 제공)
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p join fetch p.member")
    List<Post> findAllWithMember();

    @Query("select count(p) from Post p")
    long countPosts();
}

