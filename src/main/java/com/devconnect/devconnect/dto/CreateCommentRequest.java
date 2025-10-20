package com.devconnect.devconnect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateCommentRequest {
    @NotBlank
    @Size(max = 2000)
    private String text;

    private Long parentCommentId;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public Long getParentCommentId() { return parentCommentId; }
    public void setParentCommentId(Long parentCommentId) { this.parentCommentId = parentCommentId; }
}
