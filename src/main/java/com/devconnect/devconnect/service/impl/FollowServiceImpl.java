package com.devconnect.devconnect.service.impl;

import com.devconnect.devconnect.model.Follow;
import com.devconnect.devconnect.model.User;
import com.devconnect.devconnect.repository.FollowRepository;
import com.devconnect.devconnect.repository.UserRepository;
import com.devconnect.devconnect.service.FollowService;
import com.devconnect.devconnect.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void follow(Long followerId, Long followingId) {
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) return;
        User follower = userRepository.findById(followerId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User following = userRepository.findById(followingId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Follow f = new Follow();
        f.setFollower(follower);
        f.setFollowing(following);
        followRepository.save(f);
    }

    @Override
    public void unfollow(Long followerId, Long followingId) {
        followRepository.findByFollowerIdAndFollowingId(followerId, followingId).ifPresent(followRepository::delete);
    }

    @Override
    public List<User> getFollowers(Long userId) {
        return followRepository.findByFollowingId(userId).stream().map(Follow::getFollower).collect(Collectors.toList());
    }

    @Override
    public List<User> getFollowing(Long userId) {
        return followRepository.findByFollowerId(userId).stream().map(Follow::getFollowing).collect(Collectors.toList());
    }

    @Override
    public org.springframework.data.domain.Page<User> getFollowers(Long userId, org.springframework.data.domain.Pageable pageable) {
        return followRepository.findByFollowingId(userId, pageable).map(Follow::getFollower);
    }

    @Override
    public org.springframework.data.domain.Page<User> getFollowing(Long userId, org.springframework.data.domain.Pageable pageable) {
        return followRepository.findByFollowerId(userId, pageable).map(Follow::getFollowing);
    }
}
