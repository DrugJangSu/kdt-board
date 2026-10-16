package com.kdt.board;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post save(Post post) {
        return this.postRepository.save(post);
    }

    public List<Post> findAll() {
        return this.postRepository.findAll();
    }

    public Post findById(Long id) {
        return this.postRepository.findById(id).orElse(null);
    }

    public void deletePost(Long id) {
        Post post = this.postRepository.findById(id).orElse(null);
        if (post == null) {
            System.out.println("error");
        }
        this.postRepository.delete(post);
    }

}