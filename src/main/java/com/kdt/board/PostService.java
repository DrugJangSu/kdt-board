package com.kdt.board;


import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// 게시글 저장/조회/삭제 서비스
@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
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
        }
        this.postRepository.delete(post);
    }

    // 트랜잭션 안에서 제목과 작성자 loginId를 함께 조회 (없으면 "missing")
    @Transactional(readOnly = true)
    public String findTitleWithLoginId(Long id) {
        Post post = this.postRepository.findById(id).orElse(null);
        if (post == null) {
            return "missing";
        }
        return post.getTitle() + "by" + post.getMember().getLoginId();
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

    // 전체 게시글을 메모리로 로딩한 뒤 size()로 개수 계산 (countPosts와 비교용)
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

    @Transactional(readOnly = true)
    public List<PostResponse> listResponse() {
        List<PostResponse> result = new ArrayList<>();
        for (Post post : this.postRepository.findAll()) {
            PostResponse pr = toResponse(post);
            result.add(pr);
        }
        return result;
    }

    private PostResponse toResponse(Post post) {
        String loginId = post.getMember().getLoginId();
        return new PostResponse(post.getId(), post.getTitle(), loginId, post.getCreatedAt());
    }
}