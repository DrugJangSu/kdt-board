package com.kdt.board;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}