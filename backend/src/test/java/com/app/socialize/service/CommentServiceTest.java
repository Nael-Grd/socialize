package com.app.socialize.service;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import com.app.socialize.dto.CommentResponse;
import com.app.socialize.mapper.CommentMapper;
import com.app.socialize.model.Comment;
import com.app.socialize.model.Post;
import com.app.socialize.model.User;
import com.app.socialize.repository.CommentRepository;
import com.app.socialize.repository.PostRepository;
import com.app.socialize.repository.UserRepository;
import com.app.socialize.util.SecurityUtils;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

	@Mock
	private CommentRepository commentRepo;
	
	@Mock
	private UserRepository userRepo;
	
	@Mock
	private PostRepository postRepo;
	
	@Mock
	private CommentMapper commentMapper; 
	
	@InjectMocks
	private CommentService commentService;
	
	@Test
	void addComment_Succes() {
		Long id = 1L;
		String content = "Hello World!";
		String email = "n@g";
		String username = "Naël";
		
		Post post = new Post();
		User user = new User();

	    Comment savedComment = new Comment(content, user, post);     
	    CommentResponse mockResponse = new CommentResponse(1L, content, username, LocalDateTime.now());
		
		when(userRepo.findByEmail(email)).thenReturn(java.util.Optional.of(user));
		when(postRepo.findById(id)).thenReturn(java.util.Optional.of(post));	
		when(commentRepo.save(org.mockito.ArgumentMatchers.any(Comment.class))).thenReturn(savedComment);   // le save
	    when(commentMapper.toResponse(savedComment)).thenReturn(mockResponse);    // mapper
	 
	    try (org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {  // mock du current email
	        
	        mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
	        
	        CommentResponse response = commentService.addComment(content, id);
	 
	        Assertions.assertEquals(content, response.content());
	        Assertions.assertEquals(username, response.authorUsername());           
	    }	    
	}
	
	@Test
	void addComment_Fail_Email() {
		Long id = 1L;
		String content = "Hello World!";
		String email = "n@g";
		
		when(userRepo.findByEmail(email)).thenReturn(java.util.Optional.empty());  
	    
		try (org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {  
	        
	        mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
	 
	        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
		        commentService.addComment(content, id);
		    });
		    
		    Assertions.assertEquals("Utilisateur non trouvé", exception.getMessage());          
	    }	
	}
	
	@Test
	void addComment_Fail_Post() {
		Long id = 1L;
		String content = "Hello World!";
		String email = "n@g";
		
		User user = new User();
		
		when(userRepo.findByEmail(email)).thenReturn(java.util.Optional.of(user));  
		when(postRepo.findById(id)).thenReturn(java.util.Optional.empty());
	    
		try (org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {  
	        
	        mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
	 
	        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
		        commentService.addComment(content, id);
		    });
		    
		    Assertions.assertEquals("Post non trouvé", exception.getMessage());          
	    }	
	}
	
	@Test
	void deleteComment_Succes() {
		String email = "n@l";
		Long id = 1L;
		User author = new User();
		author.setEmail(email);
		Comment comment = new Comment();
		comment.setAuthor(author);
		
		when(commentRepo.findById(id)).thenReturn(java.util.Optional.of(comment));
		
		try (org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			commentService.deleteComment(id);
			
			org.mockito.Mockito.verify(commentRepo, org.mockito.Mockito.times(1)).delete(comment);
		}
	}
	
	@Test
	void deleteComment_Fail_Comment() {
		String email = "n@l";
		Long id = 1L;
		
		when(commentRepo.findById(id)).thenReturn(java.util.Optional.empty());
		
		try (org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
				commentService.deleteComment(id);
		    });
				
			Assertions.assertEquals("Commentaire non trouvé !", exception.getMessage());   
		}
	}
	
	@Test
	void deleteComment_Fail_Author() {
		String email = "n@l";
		Long id = 1L;
		
		User user = new User();
		user.setEmail("mauvais@email");
		
		Comment comment = new Comment();
		comment.setAuthor(user);
		
		when(commentRepo.findById(id)).thenReturn(java.util.Optional.of(comment));
		
		try (org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
				commentService.deleteComment(id);
		    });
				
			Assertions.assertEquals("Action non autorisée !", exception.getMessage());   
		}
	}
	
	@Test
	void updateComment_Succes() {
		String email = "n@g";
		String username = "Naël";
		Long id = 1L;
		String old_content = "Old content";
		String new_content = "New content";
		
		User author = new User();
		author.setEmail(email);
		Comment comment = new Comment();
		comment.setAuthor(author);
		Post post = new Post();
		 
		CommentResponse mockResponse = new CommentResponse(id, new_content, username, LocalDateTime.now()); // reponse attendue
		
		when(commentRepo.findById(id)).thenReturn(java.util.Optional.of(comment));
		// On utilise any() car l'objet va etre modifie en interne avant le save
	    when(commentRepo.save(org.mockito.ArgumentMatchers.any(Comment.class))).thenReturn(comment);
	    when(commentMapper.toResponse(org.mockito.ArgumentMatchers.any(Comment.class))).thenReturn(mockResponse);
	    
		try (org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			CommentResponse response = commentService.updateComment(id, "New content");
			
			Assertions.assertEquals(new_content, response.content());
			Assertions.assertEquals(new_content, comment.getContent());
			
		}
	}
	
	@Test
	void updateComment_Fail_Comment() {
		Long id = 1L;
		String email = "n@g";
		
		when(commentRepo.findById(id)).thenReturn(java.util.Optional.empty());
		
		try (org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
				commentService.updateComment(id, "New content");
			});
			
			Assertions.assertEquals("Commentaire non trouvé", exception.getMessage());		
		}
	}
	
	@Test
	void updateComment_Fail_Email() {
		Long id = 1L;
		String email = "n@g";
		String wrong_email = "wrong@email";
		
		User author = new User();
		author.setEmail(wrong_email);
		Comment comment = new Comment();
		comment.setAuthor(author);
		
		when(commentRepo.findById(id)).thenReturn(java.util.Optional.of(comment));
		
		try (org.mockito.MockedStatic<SecurityUtils> mockedSecurity = org.mockito.Mockito.mockStatic(SecurityUtils.class)) {
			mockedSecurity.when(SecurityUtils::getCurrentUserEmail).thenReturn(email);
			
			RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
				commentService.updateComment(id, "New content");
			});
			
			Assertions.assertEquals("Non autorisé à modifier ce commentaire", exception.getMessage());		
		}
	}
	
//	@Transactional
//    public CommentResponse updateComment(Long commentId, String newContent) {
//    	String currentEmail = SecurityUtils.getCurrentUserEmail();
//    	
//        Comment comment = commentRepo.findById(commentId)
//                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé"));
//        
//        if (!comment.getAuthor().getEmail().equals(currentEmail)) {
//            throw new RuntimeException("Non autorisé à modifier ce commentaire");
//        }
//        
//        comment.setContent(newContent);
//        comment = commentRepo.save(comment);
//        return commentMapper.toResponse(comment); 
//    }

}
