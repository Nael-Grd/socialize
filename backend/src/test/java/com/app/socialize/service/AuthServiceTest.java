package com.app.socialize.service;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.socialize.dto.AuthResponse;
import com.app.socialize.dto.LoginRequest;
import com.app.socialize.dto.RegisterRequest;
import com.app.socialize.model.User;
import com.app.socialize.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

	@Mock
	UserRepository userRepository;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@Mock
	JwtService jwtService;
	
	@InjectMocks
	AuthService authService;
	
	@Test
	void registerSuccess() {
		String email = "n@a.com";    // preparation des infos
	    String password = "1234";
	    String username = "Naël";
		
	    when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty());  // simulation email non existant
	    when(passwordEncoder.encode(password)).thenReturn("Mocked_Encoded_Password");
	    when(jwtService.generateToken(email)).thenReturn("My_Mocked_Token");
		
	    RegisterRequest request = new RegisterRequest(username, email, password);
	    AuthResponse response = authService.register(request);
	    
	    Assertions.assertEquals("My_Mocked_Token", response.token());
	    Assertions.assertEquals(username, response.username());
		
	}
	
	@Test
	void registerFail() {
		String email = "n@a.com";    
	    String password = "1234";
	    String username = "Naël"; 
	    User user = new User();
	    user.setEmail(email);
		
	    when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));  // simulation email existant
	
	    RegisterRequest request = new RegisterRequest(username, email, password);
	    
	    RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
	        authService.register(request);
	    });
	    
	    Assertions.assertEquals("Cet email est déjà utilisé !", exception.getMessage());
	}
	
	@Test
	void loginSuccess() {
		String email = "n@a.com";    
	    String password = "1234";   // mdp de la request
	    String username = "Naël";
	    User user = new User(username, email);
	    user.setPassword("Hashed_password");
		
	    when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));
	    when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
	    when(jwtService.generateToken(email)).thenReturn("My_Mocked_Token"); 
		
	    LoginRequest request = new LoginRequest(email, password);
	    AuthResponse response = authService.login(request);
	    
	    Assertions.assertEquals("My_Mocked_Token", response.token());
	    Assertions.assertEquals(username, response.username());		
	}
	
	@Test
	void loginFail_Non_Existing_Email() {
		String email = "n@a.com";    
	    String password = "1234";
		
	    when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.empty()); 
	
	    LoginRequest request = new LoginRequest(email, password);
	    
	    RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
	        authService.login(request);
	    });
	    
	    Assertions.assertEquals("Email introuvable !", exception.getMessage());
	}
	
	@Test
	void loginFail_Wrong_Password() {
		String email = "n@a.com";    
	    String mauvaisPassword = "0";
	    User user = new User();
	    user.setEmail(email);
	    user.setPassword("MotDePasseHasheEnBase");
		
	    when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(user));  // l'email existe
	    
	    when(passwordEncoder.matches(mauvaisPassword, user.getPassword())).thenReturn(false);   // mauvais mdp

	    LoginRequest request = new LoginRequest(email, mauvaisPassword);
	    
	    RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
	        authService.login(request);
	    });
	    
	    Assertions.assertEquals("Mot de passe incorrect", exception.getMessage());
	}
	
}


