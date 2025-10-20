package com.devconnect.devconnect.controller;

import com.devconnect.devconnect.dto.CommentDto;
import com.devconnect.devconnect.dto.CommentMapper;
import com.devconnect.devconnect.dto.CreateCommentRequest;
import com.devconnect.devconnect.model.Comment;
import com.devconnect.devconnect.service.CommentService;
import com.devconnect.devconnect.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentDto> list(@PathVariable Long postId) {
        return commentService.listByPost(postId).stream().map(CommentMapper::toDto).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable Long postId, @Valid @RequestBody CreateCommentRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        Comment c = commentService.create(postId, req, userId);
        return CommentMapper.toDto(c);
    }
}
