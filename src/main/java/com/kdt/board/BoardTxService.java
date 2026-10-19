package com.kdt.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardTxService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Autowired
    public BoardTxService(MemberRepository memberRepository, PostRepository postRepository) {
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

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
}