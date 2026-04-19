package com.app.socialize.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private JwtAuthenticationFilter filter;
	
	public SecurityConfig(JwtAuthenticationFilter filter) {
		this.filter = filter;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable()) // Désactive le CSRF pour les API
	        .cors(cors -> cors.configurationSource(request -> {
	            org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
	            config.setAllowedOrigins(java.util.Arrays.asList("https://socialize-network.vercel.app"));
	            config.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	            config.setAllowedHeaders(java.util.Arrays.asList("*"));
	            config.setAllowCredentials(true); 
	            return config;
	        }))
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/api/auth/**").permitAll() // Autorise l'inscription/login
	            .anyRequest().authenticated()
	        )
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

	    return http.build();
	}
	
	@Bean
	public PasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
         
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));   // autoriser url react
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

}
