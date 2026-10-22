package com.kdt.board;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 회원 저장/조회 서비스
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    // 스프링이 MemberRepository를 자동으로 넣어주는 생성자예요(의존성 주입).
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 저장
    public Member save(Member member) {
        return this.memberRepository.save(member);
    }
    // 전체 회원 조회
    public List<Member> findAll() {
        return this.memberRepository.findAll();
    }
    // ID로 조회 (없으면 null)
    public Member findById(Long id) {
//        this.memberRepository.findByNickname();
        return this.memberRepository.findById(id).orElse(null);
    }

    // 로그인 ID로 조회 (없으면 null)
    public Member findByLoginId(String loginId) {
        return this.memberRepository.findByLoginId(loginId).orElse(null);
    }


    // 닉네임으로 조회 (없으면 "no" 출력 후 null)
    public List<Member> findByNickname(String nickname) {
        List<Member> members = this.memberRepository.findByNickname(nickname);
        if (members.isEmpty()) {
            System.out.println("no");
            return null;
        }
        return members;
    }
//    @Transactional
//    public void register(Member member, Post post) {
//        Member saved = this.memberRepository.save(member);
//        post.setMember(saved);
//        this.postRepository.save(post);    // ← 46번 줄. MemberService엔 postRepository가 없음
//    }
}
