package com.app.socialize.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.socialize.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
