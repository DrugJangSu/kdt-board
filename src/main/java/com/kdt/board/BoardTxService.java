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
    // 회원과 게시글을 저장한 뒤 일부러 예외를 던짐
    // 다른 클래스에서 프록시를 거쳐 호출되면 롤백됨 (this로 호출되면 롤백 안 됨)
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

    // @Transactional 없이 같은 클래스의 registerFail()을 this로 호출
    // 프록시를 거치지 않아 registerFail의 @Transactional도 무시됨
    // save마다 각자 커밋되어, 예외가 나도 회원·게시글이 DB에 남음
    public String failBySelf() {
        return this.registerFail();
    }
    // @Transactional 없이 회원만 저장한 뒤 예외를 던짐
    // 트랜잭션이 없어 롤백되지 않으므로 예외가 나도 회원이 DB에 남음
    public String registerOpen() {
        String loginId = "kdt " + System.currentTimeMillis();
        Member member = new Member();
        member.setLoginId(loginId);
        member.setNickname("kimbap");
        this.memberRepository.save(member);
        System.out.println("tx open" + loginId);
        throw new IllegalStateException(loginId);
    }
    // 더티 체킹 실습: save 후 닉네임만 바꿔도(save 재호출 없이) 커밋 때 UPDATE가 자동 실행됨
    // 트랜잭션 안에서 조회·저장된 엔티티는 변경이 감지되기 때문
    @Transactional
    public String registerDirty() {
        String loginId = "dirty " + System.currentTimeMillis();
        Member member = new Member(loginId, "first");
        Member saved = this.memberRepository.save(member);
        System.out.println("dirty before " + saved.getNickname());
        saved.setNickname("second");
        System.out.println("dirty after " + saved.getNickname());
        return "dirty " + saved.getLoginId();
    }
    // flush 후 롤백 실습: 저장·닉네임 변경 뒤 flush()로 SQL을 DB에 먼저 보낸 다음 예외를 던짐
    // flush는 커밋이 아니라 SQL 전송일 뿐이라, 트랜잭션이 롤백되면 전송한 내용도 모두 취소됨
    @Transactional
    public String registerFlushFail(){
        String loginId = "flush " + System.currentTimeMillis();
        Member saved = this.memberRepository.save(new Member(loginId, "before"));
        saved.setNickname("after");
        this.memberRepository.flush();
        System.out.println("flush returned" + loginId);
        throw new IllegalStateException(loginId);
    }
}