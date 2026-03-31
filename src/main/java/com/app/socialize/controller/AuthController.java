package com.app.socialize.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.socialize.dto.LoginRequest;
import com.app.socialize.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private AuthService service;
	
	public AuthController(AuthService service) {
		this.service = service;
	}
	
	@PostMapping("login")
	String login(@RequestBody LoginRequest request) {
		return service.login(request);
	}
}
