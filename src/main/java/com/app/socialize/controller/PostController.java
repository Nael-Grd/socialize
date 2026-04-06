package com.app.socialize.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.socialize.dto.PostResponse;
import com.app.socialize.model.Post;
import com.app.socialize.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	private PostService service;
	
	public PostController(PostService service) {
		this.service = service;
	}
	
	@GetMapping
	public List<PostResponse> getAllPost() {
		return service.getAllPosts();
	}
	
	@PostMapping
	public PostResponse addPost(@RequestBody Post post) {
		return service.createPost(post);
	}
	
	@DeleteMapping("/{id}")
	public void deletePost(@PathVariable Long id) {
		service.deletePost(id);
	}
	
	@GetMapping("/feed")
	public Page<PostResponse> getFeed(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
		return service.getFeed(page, size);
	}
}
