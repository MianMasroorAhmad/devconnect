package com.devconnect.devconnect.dto;

import java.time.LocalDateTime;

public class PostDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private UserDto author;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public UserDto getAuthor() { return author; }
    public void setAuthor(UserDto author) { this.author = author; }
}
