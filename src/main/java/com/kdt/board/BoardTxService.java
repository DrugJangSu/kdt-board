package com.kdt.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 회원+게시글을 한 트랜잭션으로 저장하는 서비스 (롤백 실습용)
@Service
public class BoardTxService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Autowired
    public BoardTxService(MemberRepository memberRepository, PostRepository postRepository) {
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    // 회원과 게시글을 저장하고 정상 종료(커밋)
    @Transactional
    public String registerOk() {
        String loginId = "kdt" + System.currentTimeMillis();
        Member member = new Member();
        member.setLoginId(loginId);
        member.setNickname("kimbap");
        Member saved = this.memberRepository.save(member);

        Post post = new Post();
        post.setTitle("closed");
        post.setBody("no class");
        post.setMember(saved);
        this.postRepository.save(post);

        return "ok" + loginId;
    }

    @Transactional
    // 회원과 게시글을 저장한 뒤 일부러 예외를 던져 롤백시킴
    public String registerFail() {
        String loginId = "kdt " + System.currentTimeMillis();
        Member member = new Member();
        member.setLoginId(loginId);
        member.setNickname("kimbap");
        Member saved = this.memberRepository.save(member);

        Post post = new Post();
        post.setTitle("closed");
        post.setBody("no class");
        post.setMember(saved);
        this.postRepository.save(post);

        System.out.println("tx fail " + loginId);

        throw new IllegalStateException(loginId);
    }

    // @Transactional 없이 같은 클래스의 registerFail()을 직접 호출 (프록시를 거치지 않아 롤백 안 됨)
    public String failBySelf() {
        return this.registerFail();
    }
}