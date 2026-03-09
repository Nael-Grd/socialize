package com.training.social_network.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.social_network.model.Post;
import com.training.social_network.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private PostService service;
	
	public PostController(PostService service) {
		this.service = service;
	}
	
	@GetMapping
	public List<Post> getPosts() {
		return service.getAllPosts();
	}
	
	@PostMapping
	public Post addPost(@RequestBody Post post) {
		return service.createPost(post);
	}
}
