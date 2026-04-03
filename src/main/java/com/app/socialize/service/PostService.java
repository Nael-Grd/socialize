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
		String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		User currentUser = userRepository.findByEmail(currentUserEmail)
	            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
	    post.setAuthor(currentUser);  
	    post.setCreatedAt(LocalDateTime.now());
		return postRepository.save(post);
	}
	
	public void deletePost(Long postId) {
		
		Post post = postRepository.findById(postId).orElseThrow(
				() -> new RuntimeException("Impossible de supprimer : le post  " + postId + " n'existe pas !"));
		String authorEmail = post.getAuthor().getEmail();
		String contextEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		if (!authorEmail.equals(contextEmail)) {
	        throw new RuntimeException("Action non autorisée : vous ne pouvez supprimer que vos propres posts !");
	    }
		postRepository.deleteById(postId);
	}
	
	public Page<PostResponse> getFeed(int page, int size) {
		
		String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();		
		User currentUser = userRepository.findByEmail(currentUserEmail)
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
