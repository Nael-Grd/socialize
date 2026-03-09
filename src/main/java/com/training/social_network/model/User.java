package com.training.social_network.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;
    
    public User() {}
    public User(String username, String email) { this.username = username; this.email = email; }
    
    public Long getId() {return id;}
    public String getUsername() {return username;}
    public String getEmail() {return email;}
    
    public void setId(Long id) {this.id = id;}
    public void setUsername(String username) {this.username = username;}
    public void setEmail(String email) {this.email = email;}
    
    


}
