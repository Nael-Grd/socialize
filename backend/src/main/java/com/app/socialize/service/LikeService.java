package com.app.socialize.service;

import java.util.Optional;
import org.springframework.stereotype.Service;

import com.app.socialize.model.Like;
import com.app.socialize.model.Post;
import com.app.socialize.model.User;
import com.app.socialize.repository.LikeRepository;
import com.app.socialize.repository.PostRepository;
import com.app.socialize.repository.UserRepository;
import com.app.socialize.util.SecurityUtils;

@Service
public class LikeService {
	
	private LikeRepository likeRepo;
	private PostRepository postRepo;
	private UserRepository userRepo;
	
	public LikeService(LikeRepository likeRepo, PostRepository postRepo, UserRepository userRepo) {
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }
	
    public boolean likePost(Long post_id) {
		
		String currentEmail = SecurityUtils.getCurrentUserEmail();
		User user = userRepo.findByEmail(currentEmail).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
		Post post = postRepo.findById(post_id).orElseThrow(() -> new RuntimeException("Post non trouvé"));
		
		Optional<Like> existingLike = likeRepo.findByUserAndPost(user, post);
		
		if (existingLike.isPresent()) {
            likeRepo.delete(existingLike.get());
            return false; 
		} else {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepo.save(like);
            return true; 
        }
	}
}