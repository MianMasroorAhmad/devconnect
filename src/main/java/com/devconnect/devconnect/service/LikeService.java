package com.devconnect.devconnect.service;

public interface LikeService {
    void likePost(Long userId, Long postId);
    void unlikePost(Long userId, Long postId);
    long countLikes(Long postId);
}
