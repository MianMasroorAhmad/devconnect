package com.devconnect.devconnect.controller;

import com.devconnect.devconnect.service.LikeService;
import com.devconnect.devconnect.security.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts/{postId}/like")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ResponseEntity<?> like(@PathVariable Long postId) {
        Long userId = SecurityUtils.getCurrentUserId();
        likeService.likePost(userId, postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> unlike(@PathVariable Long postId) {
        Long userId = SecurityUtils.getCurrentUserId();
        likeService.unlikePost(userId, postId);
        return ResponseEntity.noContent().build();
    }
}
