package com.app.socialize.service;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.socialize.model.Like;
import com.app.socialize.model.Post;
import com.app.socialize.model.User;
import com.app.socialize.repository.LikeRepository;
import com.app.socialize.repository.PostRepository;
import com.app.socialize.repository.UserRepository;
import com.app.socialize.util.SecurityUtils;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {
	
	@Mock
	private LikeRepository likeRepo;
	
	@Mock
	private PostRepository postRepo;
	
	@Mock
	private UserRepository userRepo;
	
	@InjectMocks
	private LikeService likeService;
	
	@Test
	void likePost_if() {
		String email = "n@g";
		Long id = 1L;
		
		User user = new User();
		Post post = new Post();
		Like like = new Like();
		
		when(userRepo.findByEmail(email)).thenReturn(java.util.Optional.of(user));
		when(postRepo.findById(id)).thenReturn(java.util.Optional.of(post));
		when(likeRepo.findByUserAndPost(user, post)).thenReturn(java.util.Optional.of(like));   // true pour le if
		
		try (org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {  
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
	        
	        boolean isLiked = likeService.likePost(id);
	 
	        Assertions.assertFalse(isLiked); 
	        org.mockito.Mockito.verify(likeRepo, org.mockito.Mockito.times(1)).delete(like);   // verif du repo.delete (void)
	    }
	}
	
	@Test
	void likePost_else() {
		String email = "n@g";
		Long id = 1L;
		
		User user = new User();
		Post post = new Post();
		
		when(userRepo.findByEmail(email)).thenReturn(java.util.Optional.of(user));
		when(postRepo.findById(id)).thenReturn(java.util.Optional.of(post));
		when(likeRepo.findByUserAndPost(user, post)).thenReturn(java.util.Optional.empty());   //false pour le else

		org.mockito.ArgumentCaptor<Like> likeCaptor = org.mockito.ArgumentCaptor.forClass(Like.class);   // capter le like cree dans likPost()
		
		try (org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {  
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
	        
	        boolean isLiked = likeService.likePost(id);
	 
	        Assertions.assertTrue(isLiked); 
	        
	        org.mockito.Mockito.verify(likeRepo).save(likeCaptor.capture());  // verifie que le like a ete capturer
	        Like capturedLike = likeCaptor.getValue();
	        
	        Assertions.assertEquals(user, capturedLike.getUser());
	        Assertions.assertEquals(post, capturedLike.getPost());
	    }
	}
	
	@Test
	void likePost_Fail_User() {
		String email = "n@g";
		Long id = 1L;
		
		when(userRepo.findByEmail(email)).thenReturn(java.util.Optional.empty());
		
		try (org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {  
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
	        
			RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
	        	likeService.likePost(id);
	        });
	 
	        Assertions.assertEquals("Utilisateur non trouvé", exception.getMessage());         
	    }
	}
	
	@Test
	void likePost_Fail_Post() {
		String email = "n@g";
		Long id = 1L;
		
		User user = new User();
		
		when(userRepo.findByEmail(email)).thenReturn(java.util.Optional.of(user));
		when(postRepo.findById(id)).thenReturn(java.util.Optional.empty());
		
		try (org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {  
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
	        
			RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
	        	likeService.likePost(id);
	        });
	 
	        Assertions.assertEquals("Post non trouvé", exception.getMessage());         
	    }
	}


}
