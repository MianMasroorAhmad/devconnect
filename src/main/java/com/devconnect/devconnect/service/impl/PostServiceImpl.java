package com.devconnect.devconnect.service.impl;

import com.devconnect.devconnect.dto.CreatePostRequest;
import com.devconnect.devconnect.model.Post;
import com.devconnect.devconnect.model.User;
import com.devconnect.devconnect.repository.PostRepository;
import com.devconnect.devconnect.repository.UserRepository;
import com.devconnect.devconnect.service.PostService;
import com.devconnect.devconnect.service.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<Post> list(Pageable pageable) {
        return postRepository.findByDeletedFalse(pageable);
    }

    @Override
    public Post get(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

    @Override
    public Post create(CreatePostRequest req, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post p = new Post();
        p.setContent(req.getContent());
        p.setImageUrl(req.getImageUrl());
        p.setUser(user);
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(p);
    }

    @Override
    public Post update(Long id, CreatePostRequest req, Long userId) {
        Post p = get(id);
        if (!p.getUser().getId().equals(userId)) throw new IllegalStateException("Not owner");
        p.setContent(req.getContent());
        p.setImageUrl(req.getImageUrl());
        p.setUpdatedAt(LocalDateTime.now());
        return postRepository.save(p);
    }

    @Override
    public void delete(Long id, Long userId) {
        Post p = get(id);
        if (!p.getUser().getId().equals(userId)) throw new IllegalStateException("Not owner");
        p.setDeleted(true);
        postRepository.save(p);
    }
}
