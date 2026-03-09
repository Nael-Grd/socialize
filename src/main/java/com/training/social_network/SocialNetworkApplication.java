package com.training.social_network;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.training.social_network.model.User;
import com.training.social_network.repository.UserRepository;

@SpringBootApplication
public class SocialNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialNetworkApplication.class, args);
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
