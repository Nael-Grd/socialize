package com.training.social_network.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.training.social_network.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

}
