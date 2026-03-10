package com.app.socialize.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.socialize.model.Like;
import com.app.socialize.model.Post;
import com.app.socialize.model.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long>{

	// Spring genere : SELECT COUNT(*) FROM likes WHERE post_id = ?
	long countByPost(Post post);

	// Spring genere : SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM likes WHERE user_id = ? AND post_id = ?
	boolean existsByUserAndPost(User user, Post post);
}
