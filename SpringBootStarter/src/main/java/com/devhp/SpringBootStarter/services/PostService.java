package com.devhp.SpringBootStarter.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devhp.SpringBootStarter.models.Post;
import com.devhp.SpringBootStarter.repositories.PostRepository;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public void delete(Post post) {
        postRepository.delete(post);
    }

    public Post save(Post post) {
        if (post.getId() == null) {
            post.setCreateAt(LocalDateTime.now());
        }
        return postRepository.save(post);
    }
}
