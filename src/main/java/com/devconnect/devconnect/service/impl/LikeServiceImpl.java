package com.devconnect.devconnect.service.impl;

import com.devconnect.devconnect.model.Like;
import com.devconnect.devconnect.model.Post;
import com.devconnect.devconnect.model.User;
import com.devconnect.devconnect.repository.LikeRepository;
import com.devconnect.devconnect.repository.PostRepository;
import com.devconnect.devconnect.repository.UserRepository;
import com.devconnect.devconnect.service.LikeService;
import com.devconnect.devconnect.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void likePost(Long userId, Long postId) {
        if (likeRepository.existsByUserIdAndPostId(userId, postId)) return;
        User u = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Post p = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        Like l = new Like();
        l.setUser(u);
        l.setPost(p);
        likeRepository.save(l);
    }

    @Override
    public void unlikePost(Long userId, Long postId) {
        likeRepository.findByUserIdAndPostId(userId, postId).ifPresent(likeRepository::delete);
    }

    @Override
    public long countLikes(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
