package com.devconnect.devconnect.dto;

import com.devconnect.devconnect.model.Comment;

public class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        if (comment == null) return null;
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setAuthor(PostMapper.userToDto(comment.getUser()));
        dto.setParentCommentId(comment.getParentComment()!=null?comment.getParentComment().getId():null);
        return dto;
    }
}
