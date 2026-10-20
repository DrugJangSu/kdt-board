package com.kdt.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// 게시글 DB 접근 인터페이스 (기본 CRUD 제공)
public interface PostRepository extends JpaRepository<Post, Long> {

    // 게시글과 작성자를 join fetch로 한 번에 조회 (N+1 방지)
    @Query("select p from Post p join fetch p.member")
    List<Post> findAllWithMember();

    // 전체 게시글 수를 count 쿼리로 조회
    @Query("select count(p) from Post p")
    long countPosts();

    // (미사용) 제목 일치 + like 조건을 함께 거는 예시 쿼리
//    @Query("select count(p) from Post p where p.title = :title and p.title like :test")
//    long countByTitle(@Param("title") String title, @Param("test") String test);

    // 제목이 정확히 일치하는 게시글 수를 조회 (:title 파라미터 바인딩)
    @Query("select count(p) from Post p where p.title = :title")
    long countByTitle(@Param("title") String title);

}

