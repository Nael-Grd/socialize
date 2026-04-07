package com.app.socialize.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.socialize.dto.LoginRequest;
import com.app.socialize.dto.RegisterRequest;
import com.app.socialize.model.User;
import com.app.socialize.repository.UserRepository;

@Service
public class AuthService {

	private UserRepository repository;
	
	private PasswordEncoder passwordEncoder;
	
	private JwtService jwtService;
	
	public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}
	
	public String register(RegisterRequest request) {
		if (repository.findByEmail(request.email()).isPresent()) {
	        throw new RuntimeException("Cet email est déjà utilisé !");
	    }
		
		User newUser = new User(request.username(), request.email());
		newUser.setPassword(passwordEncoder.encode(request.password()));
		
		repository.save(newUser);
		
		return jwtService.generateToken(request.email());   // se connecte directement apres la creation du compte
		
	}
	
	public String login(LoginRequest request) {
		User user = repository.findByEmail(request.email()).orElseThrow(() -> new RuntimeException("Email introuvable !"));
		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
	        throw new RuntimeException("Mot de passe incorrect");
	    }
		
	    return jwtService.generateToken(request.email());
	}
}
