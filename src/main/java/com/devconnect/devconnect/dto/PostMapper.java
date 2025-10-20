package com.devconnect.devconnect.dto;

import com.devconnect.devconnect.model.Post;
import com.devconnect.devconnect.model.User;

public class PostMapper {

    public static PostDto toDto(Post post) {
        if (post == null) return null;
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setAuthor(userToDto(post.getUser()));
        return dto;
    }

    public static UserDto userToDto(User user) {
        if (user == null) return null;
        UserDto u = new UserDto();
        u.setId(user.getId());
        u.setUsername(user.getUsername());
        u.setEmail(user.getEmail());
        u.setBio(user.getBio());
        u.setProfilePictureUrl(user.getProfilePictureUrl());
        u.setRole(user.getRole());
        u.setCreatedAt(user.getCreatedAt());
        return u;
    }
}
