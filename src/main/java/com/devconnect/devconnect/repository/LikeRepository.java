package com.devconnect.devconnect.repository;

import com.devconnect.devconnect.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
	boolean existsByUserIdAndPostId(Long userId, Long postId);
	Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
	long countByPostId(Long postId);
}
