package com.app.socialize.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.socialize.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	
	@Query("SELECT p FROM Post p JOIN p.author u JOIN u.followers f WHERE f.id = :userId ORDER BY p.createdAt DESC")
	Page<Post> findFeedByUserId(@Param("userId") Long userId, Pageable pageable);

}
