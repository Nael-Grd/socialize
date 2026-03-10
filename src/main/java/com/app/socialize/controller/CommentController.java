package com.app.socialize.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.socialize.dto.CommentRequest;
import com.app.socialize.dto.LikeRequest;
import com.app.socialize.model.Like;
import com.app.socialize.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

	private CommentService service;
	
	public CommentController(CommentService service) {
		this.service = service;
	}
	
	@PostMapping
	public Comment addComment(@RequestBody CommentRequest request) {
		return service.addComment(request.content(), request.userId(), request.postId());
	}
}
