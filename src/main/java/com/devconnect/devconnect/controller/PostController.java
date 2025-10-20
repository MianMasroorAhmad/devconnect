package com.devconnect.devconnect.controller;

import com.devconnect.devconnect.dto.CreatePostRequest;
import com.devconnect.devconnect.security.SecurityUtils;
import com.devconnect.devconnect.dto.PostDto;
import com.devconnect.devconnect.dto.PostMapper;
import com.devconnect.devconnect.model.Post;
import com.devconnect.devconnect.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostDto> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Post> p = postService.list(PageRequest.of(page, size));
        return p.stream().map(PostMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PostDto get(@PathVariable Long id) {
        Post p = postService.get(id);
        return PostMapper.toDto(p);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto create(@Valid @RequestBody CreatePostRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        Post p = postService.create(req, userId);
        return PostMapper.toDto(p);
    }

    @PutMapping("/{id}")
    public PostDto update(@PathVariable Long id, @Valid @RequestBody CreatePostRequest req) {
        Long userId = SecurityUtils.getCurrentUserId();
        Post p = postService.update(id, req, userId);
        return PostMapper.toDto(p);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        postService.delete(id, userId);
    }
}
