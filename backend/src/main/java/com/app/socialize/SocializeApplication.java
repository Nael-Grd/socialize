package com.app.socialize;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.app.socialize.model.User;
import com.app.socialize.repository.UserRepository;

@SpringBootApplication
public class SocializeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocializeApplication.class, args);
	}
	
	
//	@Bean
//	public CommandLineRunner run(UserRepository userRepository) {
//		return args -> { 
//			User user = new User("Nael", "nael.de2v@gmail.com");
//			userRepository.save(user);
//			System.out.println("Insertion réussie !");
//			System.out.println(userRepository.count() + " instance(s)");
//		};
//	}
}
