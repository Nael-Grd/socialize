package com.app.socialize.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.socialize.model.Like;
import com.app.socialize.service.LikeService;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

	private LikeService service;
	
	public LikeController(LikeService service) {
		this.service = service;
	}
	
	@PostMapping("/{postId}")
	public Like addLike(@PathVariable Long postId) {
		return service.likePost(postId);
	}
}
