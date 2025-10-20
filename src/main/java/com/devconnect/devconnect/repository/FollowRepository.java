package com.devconnect.devconnect.repository;

import com.devconnect.devconnect.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowRepository extends JpaRepository<Follow, Long> {

	boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
	Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
	List<Follow> findByFollowingId(Long userId);
	List<Follow> findByFollowerId(Long userId);
	Page<Follow> findByFollowingId(Long userId, Pageable pageable);
	Page<Follow> findByFollowerId(Long userId, Pageable pageable);


}
