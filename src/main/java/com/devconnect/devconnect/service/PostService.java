package com.devconnect.devconnect.service;

import com.devconnect.devconnect.dto.CreatePostRequest;
import com.devconnect.devconnect.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Page<Post> list(Pageable pageable);
    Post get(Long id);
    Post create(CreatePostRequest req, Long userId);
    Post update(Long id, CreatePostRequest req, Long userId);
    void delete(Long id, Long userId);
}
