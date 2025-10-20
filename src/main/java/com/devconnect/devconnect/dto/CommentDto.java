package com.devconnect.devconnect.dto;

import java.time.LocalDateTime;

public class CommentDto {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private UserDto author;
    private Long parentCommentId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public UserDto getAuthor() { return author; }
    public void setAuthor(UserDto author) { this.author = author; }
    public Long getParentCommentId() { return parentCommentId; }
    public void setParentCommentId(Long parentCommentId) { this.parentCommentId = parentCommentId; }
}
