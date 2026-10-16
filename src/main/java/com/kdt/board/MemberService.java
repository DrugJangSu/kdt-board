package com.kdt.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member save(Member member) {
        return this.memberRepository.save(member);
    }
    public List<Member> findAll() {
        return this.memberRepository.findAll();
    }
    public Member findById(Long id) {
//        this.memberRepository.findByNickname();
        return this.memberRepository.findById(id).orElse(null);
    }

//    public Member findByLoginId(String loginId) {
//
//    }

}
