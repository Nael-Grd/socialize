package com.app.socialize.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.socialize.dto.UserProfile;
import com.app.socialize.dto.UserSummary;
import com.app.socialize.model.User;
import com.app.socialize.service.UserService;

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
	
	@GetMapping("/{username}")
	public UserProfile getUserProfile(@PathVariable String username) {
		return service.getUserProfile(username);
	}
	
	@PostMapping
	public User addUser(@RequestBody User user) {
		return service.createUser(user);
	}
	
	@PostMapping("/follow/{followedId}")
	public User addFollow(@PathVariable Long followedId) {
		return service.follow(followedId);
	}
	
	@PostMapping("/unfollow/{followedId}")
	public User unfollow(@PathVariable Long followedId) {
		return service.unfollow(followedId);
	}
	
	@GetMapping("/{username}/followers")
	public List<UserSummary> getFollowers(@PathVariable String username) {
		return service.getFollowers(username);
	}
	
	@GetMapping("/{username}/following")
	public List<UserSummary> getFollowing(@PathVariable String username) {
		return service.getFollowing(username);
	}
	
	@GetMapping("/search")
	public List<UserSummary> serachUsers(@RequestParam String query) {
		return service.searchUsers(query);
	}
}
