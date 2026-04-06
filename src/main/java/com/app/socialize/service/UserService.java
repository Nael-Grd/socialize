package com.app.socialize.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.socialize.model.User;
import com.app.socialize.repository.UserRepository;

@Service
public class UserService {

	private UserRepository repository;
	
	private PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public List<User> getAllUsers() {
		return repository.findAll();
	}
	
	public User createUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return repository.save(user);
	}
	
	public User follow(Long followedId) {
		
		
		String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		User follower = repository.findByEmail(currentEmail).orElseThrow();
		if (follower.getId().equals(followedId)) {
	        throw new RuntimeException("Vous ne pouvez pas vous abonner à vous-même !");
	    }	
		User followed = repository.findById(followedId).orElseThrow();
		
		follower.getFollowing().add(followed);
		followed.getFollowers().add(follower);
		
		return repository.save(follower);
	}
	
	public User unfollow(Long followedId) {

		String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		User follower = repository.findByEmail(currentEmail).orElseThrow();
		if (follower.getId().equals(followedId)) {
	        throw new RuntimeException("Vous ne pouvez pas vous désabonner de vous-même !");
	    }	
		User followed = repository.findById(followedId).orElseThrow();
		
		follower.getFollowing().remove(followed);
		followed.getFollowers().remove(follower);
		
		return repository.save(follower);
	}
}
