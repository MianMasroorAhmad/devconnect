package com.devconnect.devconnect.service;

import com.devconnect.devconnect.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FollowService {
    void follow(Long followerId, Long followingId);
    void unfollow(Long followerId, Long followingId);
    List<User> getFollowers(Long userId);
    List<User> getFollowing(Long userId);
    Page<User> getFollowers(Long userId, Pageable pageable);
    Page<User> getFollowing(Long userId, Pageable pageable);
}
