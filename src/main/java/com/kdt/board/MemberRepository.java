package com.kdt.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


// 회원 DB 접근 인터페이스 (기본 CRUD + 아래 조회 메서드)
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 로그인 ID로 회원 한 명 조회
    Optional<Member> findByLoginId(String loginId);
    // 닉네임으로 회원 목록 조회
    List<Member> findByNickname(String nickname);
}
