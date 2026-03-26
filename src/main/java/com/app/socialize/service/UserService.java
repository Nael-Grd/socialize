package com.app.socialize.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.socialize.model.User;
import com.app.socialize.repository.UserRepository;

@Service
public class UserService {

	private UserRepository repository;
	
	public UserService(UserRepository repository) {
		this.repository = repository;
	}
	
	public List<User> getAllUsers() {
		return repository.findAll();
	}
	
	public User createUser(User user) {
		return repository.save(user);
	}
	
	public User follow(Long followerId, Long followedId) {
		if (followerId.equals(followedId)) {
	        throw new RuntimeException("Vous ne pouvez pas vous abonner à vous-même !");
	    }
		User follower = repository.findById(followerId).orElseThrow();
		User followed = repository.findById(followedId).orElseThrow();
		
		follower.getFollowing().add(followed);
		followed.getFollowers().add(follower);
		
		return repository.save(follower);
	}
}
