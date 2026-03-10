package com.app.socialize.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.socialize.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
