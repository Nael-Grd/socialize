package com.app.socialize.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.socialize.model.User;
import com.app.socialize.repository.UserRepository;
import com.app.socialize.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private JwtService jwtService;
	
	private UserRepository userRepository;
	
	public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
		this.jwtService = jwtService;
		this.userRepository = userRepository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
		throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request ,response);
			return;
		}		
		String jwt = authHeader.substring(7);
		String userEmail = jwtService.extractEmail(jwt);
		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {   // si on ne trouve pas l'email dans le contexte
		    User user = userRepository.findByEmail(userEmail).orElse(null); // on cherche l'user dans la bd    
		    if (user != null) {
		        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
		                user, 
		                null, 
		                null // Normalement, on met les "rôles" ici (ex: ADMIN, USER). On met null pour l'instant.
		        );
		        SecurityContextHolder.getContext().setAuthentication(authToken);
		    }
		}
		filterChain.doFilter(request, response);
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	    String path = request.getRequestURI();
	    return path.startsWith("/api/auth/");
	}

}
