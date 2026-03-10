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
}
