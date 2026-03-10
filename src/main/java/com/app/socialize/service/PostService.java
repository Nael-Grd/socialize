package com.app.socialize.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.socialize.model.Post;
import com.app.socialize.repository.PostRepository;

@Service
public class PostService {

	private PostRepository repository;
	
	public PostService(PostRepository repository) {
		this.repository = repository;
	}
	
	public List<Post> getAllPosts() {
		return repository.findAll();
	}
	
	public Post createPost(Post post) {
		return repository.save(post);
	}
}
