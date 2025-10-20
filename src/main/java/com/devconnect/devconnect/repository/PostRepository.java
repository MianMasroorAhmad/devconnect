package com.devconnect.devconnect.repository;

import com.devconnect.devconnect.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository extends JpaRepository<Post, Long>{

	Page<Post> findByDeletedFalse(Pageable pageable);

}

