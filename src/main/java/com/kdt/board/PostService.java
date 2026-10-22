package com.kdt.board;

import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// 게시글 저장/조회/삭제 서비스
@Service
public class PostService {

    // 게시글 DB 접근
    private final PostRepository postRepository;

    // 회원 DB 접근
    private final MemberRepository memberRepository;

    // PostRepository와 MemberRepository 둘 다 주입받음
    @Autowired
    public PostService(
            PostRepository postRepository,
            MemberRepository memberRepository
    ) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }


    // 게시글 저장
    public Post save(Post post) {
        return this.postRepository.save(post);
    }


    // 전체 게시글 조회
    public List<Post> findAll() {
        return this.postRepository.findAll();
    }


    // ID로 조회 (없으면 null)
    public Post findById(Long id) {
        return this.postRepository.findById(id).orElse(null);
    }


    // ID로 게시글 삭제
    public void deletePost(Long id) {

        Post post = this.postRepository.findById(id).orElse(null);

        if (post == null) {
            System.out.println("error");
            return;
        }

        this.postRepository.delete(post);
    }


    // 트랜잭션 안에서 제목과 작성자 loginId를 함께 조회
    // 게시글이 없으면 "missing"
    @Transactional(readOnly = true)
    public String findTitleWithLoginId(Long id) {

        Post post = this.postRepository.findById(id).orElse(null);

        if (post == null) {
            return "missing";
        }

        return post.getTitle()
                + "by"
                + post.getMember().getLoginId();
    }


    // 작성자까지 join fetch한 전체 게시글 조회
    @Transactional(readOnly = true)
    public List<Post> findAllWithMember() {
        return this.postRepository.findAllWithMember();
    }


    // DB count 쿼리로 게시글 수 조회
    @Transactional(readOnly = true)
    public long countPosts() {
        return this.postRepository.countPosts();
    }


    // 전체 게시글을 메모리에 로딩한 뒤 size()로 개수 계산
    @Transactional(readOnly = true)
    public int countLoadedPosts() {

        List<Post> posts = this.postRepository.findAll();

        return posts.size();
    }


    // 제목이 일치하는 게시글 수 조회
    @Transactional(readOnly = true)
    public long countByTitle(String title) {
        return this.postRepository.countByTitle(title);
    }


    // 전체 게시글을 PostResponse DTO로 변환해서 반환
    @Transactional(readOnly = true)
    public List<PostResponse> listResponse() {

        List<PostResponse> result = new ArrayList<>();

        for (Post post : this.postRepository.findAll()) {

            PostResponse pr = toResponse(post);

            result.add(pr);
        }

        return result;
    }


    // 게시글 하나를 PostResponse DTO로 조회
    @Transactional(readOnly = true)
    public PostResponse findResponse(Long id) {

        Post post = this.postRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("post missing")
                );

        return toResponse(post);
    }


    // Post 엔티티를 PostResponse DTO로 변환
    private PostResponse toResponse(Post post) {

        String loginId = post.getMember().getLoginId();

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                loginId,
                post.getCreatedAt()
        );
    }


    // 새로운 게시글 생성
    @Transactional
    public PostResponse create(PostCreateRequest request) {

        // 요청에서 받은 memberId로 작성자 회원 조회
        Member member = this.memberRepository
                .findById(request.memberId())
                .orElseThrow(
                        () -> new NotFoundException("member missing")
                );

        // 새로운 Post 객체 생성
        Post post = new Post();

        // 요청 DTO의 값을 Post에 넣음
        post.setTitle(request.title());
        post.setBody(request.body());

        // 위에서 찾은 회원을 게시글 작성자로 연결
        post.setMember(member);

        // DB에 저장
        Post saved = this.postRepository.save(post);

        // 저장된 Post를 응답 DTO로 변환
        return toResponse(saved);
    }

    @Transactional
    public PostResponse update(Long id, PostUpdateRequest request) {
        Post post = this.postRepository.findById(id).orElseThrow(() -> new NotFoundException("post missing"));
        post.setTitle(request.title());
        post.setBody(request.body());
        return toResponse(post);
    }
}