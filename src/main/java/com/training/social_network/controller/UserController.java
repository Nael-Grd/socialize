package com.training.social_network.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.social_network.model.User;
import com.training.social_network.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private UserService service;
	
	public UserController(UserService service) {
		this.service = service;
	}
	
	@GetMapping
	public List<User> getUsers() {
		return service.getAllUsers();
	}
	
	@PostMapping
	public User addUser(@RequestBody User user) {
		return service.createUser(user);
	}
}
