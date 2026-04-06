package com.app.socialize.service;

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
	
	public Like likePost(Long post_id) {
		
		String currentEmail = SecurityUtils.getCurrentUserEmail();
		
		User user = userRepo.findByEmail(currentEmail).orElseThrow();
		Post post = postRepo.findById(post_id).orElseThrow();
		
		if (likeRepo.existsByUserAndPost(user, post)) {
			throw new RuntimeException("Cet utilisateur à déjà liké ce post !");
		}
		Like like = new Like();
		like.setUser(user);
		like.setPost(post);
		
		return likeRepo.save(like);
	}
	

}
