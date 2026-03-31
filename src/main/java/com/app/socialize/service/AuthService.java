package com.app.socialize.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.socialize.dto.LoginRequest;
import com.app.socialize.model.User;
import com.app.socialize.repository.UserRepository;

@Service
public class AuthService {

	private UserRepository repository;
	
	private PasswordEncoder passwordEncoder;
	
	public AuthService(UserRepository repository, PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public String login(LoginRequest request) {
		User user = repository.findByEmail(request.email()).orElseThrow(() -> new RuntimeException("Email introuvable !"));
		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
	        throw new RuntimeException("Mot de passe incorrect");
	    }
		
		// Plus tard, on renverra le vrai Token JWT ici
	    return "Connexion réussie pour " + user.getUsername() + " !";
	}
}
