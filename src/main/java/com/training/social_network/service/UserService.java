package com.training.social_network.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.training.social_network.repository.UserRepository;
import com.training.social_network.model.User;

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
