package com.app.socialize.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.socialize.dto.CommentResponse;
import com.app.socialize.dto.PostResponse;
import com.app.socialize.mapper.CommentMapper; // 👈 Import du mapper
import com.app.socialize.model.Comment;
import com.app.socialize.model.Post;
import com.app.socialize.model.User;
import com.app.socialize.repository.CommentRepository;
import com.app.socialize.repository.PostRepository;
import com.app.socialize.repository.UserRepository;
import com.app.socialize.util.SecurityUtils;

@Service
public class CommentService {
	
	private final CommentRepository commentRepo;
	private final UserRepository userRepo;
	private final PostRepository postRepo;
	private final CommentMapper commentMapper; 
	
	public CommentService(CommentRepository commentRepo, UserRepository userRepo, 
                          PostRepository postRepo, CommentMapper commentMapper) {
		this.commentRepo = commentRepo;
		this.userRepo = userRepo;
		this.postRepo = postRepo;
		this.commentMapper = commentMapper;
	}
	
	public CommentResponse addComment(String content, Long post_id) {
		
		String currentEmail = SecurityUtils.getCurrentUserEmail();

		User author = userRepo.findByEmail(currentEmail)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
		Post post = postRepo.findById(post_id)
            .orElseThrow(() -> new RuntimeException("Post non trouvé"));
		
		Comment comment = new Comment(content, author, post);
		Comment savedComment = commentRepo.save(comment);
		
		return commentMapper.toResponse(savedComment);
	}
	
	@Transactional
    public void deleteComment(Long commentId) {
        String currentEmail = SecurityUtils.getCurrentUserEmail();
        
        Comment comment = commentRepo.findById(commentId).orElseThrow(
                () -> new RuntimeException("Commentaire non trouvé !"));
        
        if (!comment.getAuthor().getEmail().equals(currentEmail)) {
            throw new RuntimeException("Action non autorisée !");
        }
        
        commentRepo.delete(comment);
    }
    
    @Transactional
    public CommentResponse updateComment(Long commentId, String newContent) {
    	String currentEmail = SecurityUtils.getCurrentUserEmail();
    	
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé"));
        
        if (!comment.getAuthor().getEmail().equals(currentEmail)) {
            throw new RuntimeException("Non autorisé à modifier ce commentaire");
        }
        
        comment.setContent(newContent);
        comment = commentRepo.save(comment);
        return commentMapper.toResponse(comment); 
    }
    
}