package com.devconnect.devconnect.service;

import com.devconnect.devconnect.dto.CreateCommentRequest;
import com.devconnect.devconnect.model.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> listByPost(Long postId);
    Comment create(Long postId, CreateCommentRequest req, Long userId);
}
