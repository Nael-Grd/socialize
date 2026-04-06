package com.app.socialize.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.socialize.dto.PostResponse;
import com.app.socialize.model.Post;
import com.app.socialize.model.User;
import com.app.socialize.repository.PostRepository;
import com.app.socialize.repository.UserRepository;
import com.app.socialize.util.SecurityUtils;

@Service
public class PostService {

	private PostRepository postRepository;
	
	private UserRepository userRepository;
	
	public PostService(PostRepository postRepository, UserRepository userRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}
	
	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}
	
	public Post createPost(Post post) {
		
		String currentEmail = SecurityUtils.getCurrentUserEmail();

		User currentUser = userRepository.findByEmail(currentEmail)
	            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec cet email : " + currentEmail));
		
	    post.setAuthor(currentUser);  
	    post.setCreatedAt(LocalDateTime.now());
		return postRepository.save(post);
	}
	
public void deletePost(Long postId) {
	
	    String currentEmail = SecurityUtils.getCurrentUserEmail();
		
		Post post = postRepository.findById(postId).orElseThrow(
				() -> new RuntimeException("Impossible de supprimer : le post " + postId + " n'existe pas !"));
		
		String authorEmail = post.getAuthor().getEmail();

		if (!authorEmail.equals(currentEmail)) {
	        throw new RuntimeException("Action non autorisée : vous ne pouvez supprimer que vos propres posts !");
	    }
		
		postRepository.deleteById(postId);
	}
	
	public Page<PostResponse> getFeed(int page, int size) {
		
		String currentEmail = SecurityUtils.getCurrentUserEmail();
		
		User currentUser = userRepository.findByEmail(currentEmail)
	            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
		Pageable pageable = PageRequest.of(page, size);
		Page<Post> posts = postRepository.findFeedByUserId(currentUser.getId(), pageable);
		
		return posts.map(post -> new PostResponse(   
	            post.getId(),   
	            post.getContent(), 
	            post.getAuthor().getUsername() 
	    ));
	}
	
}
