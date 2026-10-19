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
        String loginId = "kdt" + System.currentTimeMillis(); // 현재 시각을 long time으로 반환해줌
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

}
