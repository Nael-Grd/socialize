package com.app.socialize.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. On désactive la protection CSRF (utile pour les sites web, mais gênant pour les API REST/Postman)
            .csrf(AbstractHttpConfigurer::disable)
            
            // 2. On configure les autorisations sur nos routes
            .authorizeHttpRequests(auth -> auth
            	    // On autorise UNIQUEMENT la création de compte (POST)
            	    .requestMatchers(HttpMethod.POST, "/api/users").permitAll() 
            	    
            	    // On prépare le terrain pour la future route de connexion
            	    .requestMatchers("/api/auth/login").permitAll() 
            	    
            	    // On bloque TOUT le reste (le Feed, les Likes, et même le GET /api/users)
            	    .anyRequest().authenticated()
            );
            
        return http.build();
    }

}
