package com.app.socialize.service;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.app.socialize.dto.PostResponse;
import com.app.socialize.mapper.PostMapper;
import com.app.socialize.model.Post;
import com.app.socialize.model.User;
import com.app.socialize.repository.PostRepository;
import com.app.socialize.repository.UserRepository;
import com.app.socialize.util.SecurityUtils;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

	@Mock
	private PostRepository postRepository;
	
	@Mock
    private UserRepository userRepository;
	
	@Mock
    private PostMapper postMapper; 

	@InjectMocks
	private PostService postService;
	
	@Test
	void getAllPosts_Empty() {	
		
		when(postRepository.findAll()).thenReturn(java.util.Collections.emptyList());
		
		List<PostResponse> posts = postService.getAllPosts();
		
		Assertions.assertTrue(posts.isEmpty());
		org.mockito.Mockito.verify(postMapper, org.mockito.Mockito.never()).toResponse(org.mockito.ArgumentMatchers.any());  // on verifie que le mapper n'a pas ete appele
	}
	
	@Test
	void getAllPosts_Full() {	
		
		Post post1 = new Post();
		Post post2 = new Post();
		PostResponse postResponse1 = new PostResponse(1L, "Post 1", "Naël", 0, LocalDateTime.now(), null);
		PostResponse postResponse2 = new PostResponse(2L, "Post 2", "Naël", 0, LocalDateTime.now(), null);
		
		when(postRepository.findAll()).thenReturn(java.util.List.of(post1, post2));
		when(postMapper.toResponse(post1)).thenReturn(postResponse1);
		when(postMapper.toResponse(post2)).thenReturn(postResponse2);
			
		List<PostResponse> posts = postService.getAllPosts();
		
		Assertions.assertEquals(2, posts.size());
		Assertions.assertEquals(postResponse1, posts.get(0));
		Assertions.assertEquals(postResponse2, posts.get(1));
		
		org.mockito.Mockito.verify(postMapper, org.mockito.Mockito.times(2))
        .toResponse(org.mockito.ArgumentMatchers.any(Post.class));      // 2 appels du mapper      
	}
	
	@Test
	void createPost_Sucess() {		
		String email = "n@g";
		
		User author = new User();
		Post post = new Post();
		post.setContent("Mon post");
		PostResponse postResponse = new PostResponse(1L, "Mon post", "Naël", 0, LocalDateTime.now(), null);
		
		when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(author));
		when(postRepository.save(org.mockito.ArgumentMatchers.any(Post.class))).thenReturn(post);
	    when(postMapper.toResponse(org.mockito.ArgumentMatchers.any(Post.class))).thenReturn(postResponse);
	    
	    org.mockito.ArgumentCaptor<Post> postCaptor = org.mockito.ArgumentCaptor.forClass(Post.class);
	    
		try(org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			PostResponse response = postService.createPost(post);
			
			Assertions.assertEquals(postResponse, response);
			
			org.mockito.Mockito.verify(postRepository).save(postCaptor.capture());
	        Post capturedPost = postCaptor.getValue();
	        
	        Assertions.assertEquals(author, capturedPost.getAuthor()); 
	        Assertions.assertEquals("Mon post", capturedPost.getContent());
	        Assertions.assertNotNull(capturedPost.getCreatedAt());
		}
	}
	
	@Test
	void createPost_Fail_Email() {		
		String email = "n@g";
		Post post = new Post();
		
		when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());
	    
		try(org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
				postService.createPost(post);
			});
			
			Assertions.assertEquals("Utilisateur non trouvé", exception.getMessage());
			
		}
	}
	
	@Test
	void deletePost_Succes() {
		Long id = 1L;
		String email = "n@g";
		
		User author = new User();
		author.setEmail(email);
		Post post = new Post();
		post.setAuthor(author);
		
		when(postRepository.findById(id)).thenReturn(java.util.Optional.of(post));
		
		try(org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			postService.deletePost(id);
			
			org.mockito.Mockito.verify(postRepository, org.mockito.Mockito.times(1)).delete(post);
		}	
	}
	
	@Test
	void deletePost_Fail_Post() {
		Long id = 1L;
		String email = "n@g";
		
		when(postRepository.findById(id)).thenReturn(java.util.Optional.empty());
		
		try(org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
				postService.deletePost(id);
			});
			
			Assertions.assertEquals("Post non trouvé !", exception.getMessage());
		}
		
	}
	
	@Test
	void deletePost_Fail_User() {
		Long id = 1L;
		String email = "n@g";
		String wrong_email = "wrong@email";
		
		User author = new User();
		author.setEmail(wrong_email);
		Post post = new Post();
		post.setAuthor(author);
		
		
		when(postRepository.findById(id)).thenReturn(java.util.Optional.of(post));
		
		try(org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
				postService.deletePost(id);
			});
			
			Assertions.assertEquals("Action non autorisée !", exception.getMessage());
		}
		
	}
	

	
	@Test
	void updatePost_Succes() {
		String email = "n@g";
		Long id = 1L;
		
		User author = new User();
		author.setEmail(email);
		
		Post post = new Post();
		post.setAuthor(author);
		post.setContent("Old content");
		
		PostResponse postResponse = new PostResponse(id, "New content", "Naël", 0, LocalDateTime.now(), null);
		
		when(postRepository.findById(id)).thenReturn(java.util.Optional.of(post));
		when(postRepository.save(org.mockito.ArgumentMatchers.any(Post.class))).thenReturn(post);
		when(postMapper.toResponse(post)).thenReturn(postResponse);
		
		try(org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			PostResponse response = postService.updatePost(id, "New content");
			
			Assertions.assertEquals(postResponse, response);		
			Assertions.assertEquals("New content", post.getContent());
		}
	}
	
	@Test
	void updatePost_Fail_Post() {
		String email = "n@g";
		Long id = 1L;
		
		when(postRepository.findById(id)).thenReturn(java.util.Optional.empty());
		
		try(org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
				postService.updatePost(id, "New content");
			});
							
			Assertions.assertEquals("Post non trouvé", exception.getMessage());
		}
	}
	
	@Test
	void updatePost_Fail_Email() {
		String email = "n@g";
		String wrong_email = "wrong@email";
		Long id = 1L;
		
		User author = new User();
		author.setEmail(wrong_email);
		Post post = new Post();
		post.setAuthor(author);
		
		when(postRepository.findById(id)).thenReturn(java.util.Optional.of(post));
		
		try(org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
				postService.updatePost(id, "New content");
			});
							
			Assertions.assertEquals("Non autorisé à modifier ce post", exception.getMessage());
		}
	}
	
	@Test
	void getUserPosts_Empty() {
		String username = "Naël";

		when(postRepository.findByAuthorUsernameOrderByCreatedAtDesc(
	            org.mockito.ArgumentMatchers.eq(username), 
	            org.mockito.ArgumentMatchers.any(Pageable.class) 
	    )).thenReturn(org.springframework.data.domain.Page.empty());
		
		Page<PostResponse> posts = postService.getUserPosts(username, 1, 1);
		
		Assertions.assertTrue(posts.isEmpty());
		org.mockito.Mockito.verify(postMapper, org.mockito.Mockito.never()).toResponse(org.mockito.ArgumentMatchers.any());
	}
	
	@Test
	void getUserPosts_One() {
		String username = "Naël";
		
		Post post = new Post();
		PostResponse postResponse = new PostResponse(1L, "Mon post", "Naël", 0, LocalDateTime.now(), null);
		
		when(postRepository.findByAuthorUsernameOrderByCreatedAtDesc(
	            org.mockito.ArgumentMatchers.eq(username), 
	            org.mockito.ArgumentMatchers.any(Pageable.class) 
	    )).thenReturn(new org.springframework.data.domain.PageImpl<>(java.util.List.of(post)));
		when(postMapper.toResponse(post)).thenReturn(postResponse);
		
		Page<PostResponse> posts = postService.getUserPosts(username, 1, 1);
		
		Assertions.assertEquals(1, posts.getTotalElements()); 
	    Assertions.assertEquals(postResponse, posts.getContent().get(0)); 
		org.mockito.Mockito.verify(postMapper, org.mockito.Mockito.times(1)).toResponse(org.mockito.ArgumentMatchers.any());
	}
	
	@Test
	void getUserPosts_Two() {
		String username = "Naël";
		
		Post post1 = new Post();
		Post post2 = new Post();
		PostResponse postResponse1 = new PostResponse(1L, "Mon post", "Naël", 0, LocalDateTime.now(), null);
		PostResponse postResponse2 = new PostResponse(1L, "Mon post", "Naël", 0, LocalDateTime.now(), null);
		
	
		when(postRepository.findByAuthorUsernameOrderByCreatedAtDesc(
	            org.mockito.ArgumentMatchers.eq(username), 
	            org.mockito.ArgumentMatchers.any(Pageable.class) 
	    )).thenReturn(new org.springframework.data.domain.PageImpl<>(java.util.List.of(post1, post2)));
		when(postMapper.toResponse(post1)).thenReturn(postResponse1);
		when(postMapper.toResponse(post2)).thenReturn(postResponse2);
		
		Page<PostResponse> posts = postService.getUserPosts(username, 1, 1);
		
		Assertions.assertEquals(2, posts.getTotalElements()); 
	    Assertions.assertEquals(postResponse1, posts.getContent().get(0)); 
	    Assertions.assertEquals(postResponse2, posts.getContent().get(1)); 
		org.mockito.Mockito.verify(postMapper, org.mockito.Mockito.times(2)).toResponse(org.mockito.ArgumentMatchers.any());
	}
	
	
	@Test
	void getFeed_Succes() {
		String email = "n@g";
		Long id = 1L;
		
		User user = new User();
		user.setId(id);
		Post post1 = new Post();
		Post post2 = new Post();
		PostResponse postResponse1 = new PostResponse(1L, "Post 1", "Naël", 0, null, null);
		PostResponse postResponse2 = new PostResponse(2L, "Post 2", "Naël", 0, null, null);
		
		when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));
		when(postRepository.findFeedByUserId(org.mockito.ArgumentMatchers.eq(id), org.mockito.ArgumentMatchers.any(Pageable.class)))
			.thenReturn(new org.springframework.data.domain.PageImpl<>(java.util.List.of(post1, post2)));
		when(postMapper.toResponse(post1)).thenReturn(postResponse1);
		when(postMapper.toResponse(post2)).thenReturn(postResponse2);
		
		try(org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			Page<PostResponse> response = postService.getFeed(1, 1);
			
			Assertions.assertEquals(2 ,response.getTotalElements());
			Assertions.assertEquals(postResponse1 ,response.getContent().get(0));
			Assertions.assertEquals(postResponse2 ,response.getContent().get(1));		
		}
	}
	
	@Test
	void getFeed_Fail_User() {
		String email = "n@g";
		
		when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());
		
		try(org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
				postService.getFeed(1, 1);
			});
			
			Assertions.assertEquals("Utilisateur non trouvé", exception.getMessage());
		}
	}
	
	
}
