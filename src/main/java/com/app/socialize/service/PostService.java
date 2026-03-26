package com.app.socialize.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.socialize.dto.PostResponse;
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
	
	public void deletePost(Long postId) {
		if(!repository.existsById(postId)) {
			throw new RuntimeException("Impossible de supprimer : le post  " + postId + " n'existe pas !");
		}
		repository.deleteById(postId);
	}
	
	public Page<PostResponse> getFeed(Long userId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Post> posts = repository.findFeedByUserId(userId, pageable);
		
		return posts.map(post -> new PostResponse(   // on transfome les posts en posts response
				post.getId(),   
				post.getContent(), 
				post.getAuthor().getUsername() 
	            ));
	}
}
