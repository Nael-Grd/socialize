package com.app.socialize.service;

import org.springframework.stereotype.Service;

import com.app.socialize.model.Comment;
import com.app.socialize.model.Post;
import com.app.socialize.model.User;
import com.app.socialize.repository.CommentRepository;
import com.app.socialize.repository.PostRepository;
import com.app.socialize.repository.UserRepository;
import com.app.socialize.util.SecurityUtils;

@Service
public class CommentService {
	
	private CommentRepository commentRepo;
	private UserRepository userRepo;
	private PostRepository postRepo;
	
	public CommentService(CommentRepository commentRepo, UserRepository userRepo, PostRepository postRepo) {
		this.commentRepo = commentRepo;
		this.userRepo = userRepo;
		this.postRepo = postRepo;
	}
	
	public Comment addComment(String content, Long post_id) {
		
		String currentEmail = SecurityUtils.getCurrentUserEmail();

		User author = userRepo.findByEmail(currentEmail).orElseThrow();
		Post post = postRepo.findById(post_id).orElseThrow();
		
		Comment comment = new Comment(content, author, post);
		return commentRepo.save(comment);
		
	}
	
}
