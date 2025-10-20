package com.devconnect.devconnect.service.impl;

import com.devconnect.devconnect.dto.CreateCommentRequest;
import com.devconnect.devconnect.model.Comment;
import com.devconnect.devconnect.model.Post;
import com.devconnect.devconnect.model.User;
import com.devconnect.devconnect.repository.CommentRepository;
import com.devconnect.devconnect.repository.PostRepository;
import com.devconnect.devconnect.repository.UserRepository;
import com.devconnect.devconnect.service.CommentService;
import com.devconnect.devconnect.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Comment> listByPost(Long postId) {
        postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return commentRepository.findByPostIdAndDeletedFalseOrderByCreatedAtAsc(postId);
    }

    @Override
    public Comment create(Long postId, CreateCommentRequest req, Long userId) {
        Post p = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        User u = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Comment c = new Comment();
        c.setText(req.getText());
        c.setPost(p);
        c.setUser(u);
        c.setCreatedAt(LocalDateTime.now());
        if (req.getParentCommentId() != null) {
            Comment parent = commentRepository.findById(req.getParentCommentId()).orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"));
            c.setParentComment(parent);
        }
        return commentRepository.save(c);
    }
}
