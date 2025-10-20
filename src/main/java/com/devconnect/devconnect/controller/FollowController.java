package com.devconnect.devconnect.controller;

import com.devconnect.devconnect.dto.UserDto;
import com.devconnect.devconnect.dto.PostMapper;
import org.springframework.data.domain.Page;
import com.devconnect.devconnect.security.SecurityUtils;
import com.devconnect.devconnect.service.FollowService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<?> follow(@PathVariable Long userId) {
        Long me = SecurityUtils.getCurrentUserId();
        followService.follow(me, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/follow")
    public ResponseEntity<?> unfollow(@PathVariable Long userId) {
        Long me = SecurityUtils.getCurrentUserId();
        followService.unfollow(me, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/followers")
    public Page<UserDto> followers(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return followService.getFollowers(userId, PageRequest.of(page, size)).map(PostMapper::userToDto);
    }

    @GetMapping("/{userId}/following")
    public Page<UserDto> following(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return followService.getFollowing(userId, PageRequest.of(page, size)).map(PostMapper::userToDto);
    }
}
