package com.app.socialize.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
    
    @ManyToMany
    @JoinTable(name = "user_following", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "following_id"))
    @JsonIgnoreProperties({"followers", "following"})
    private Set<User> following = new HashSet<User>();
    
    @ManyToMany(mappedBy = "following")   // miroir 
    @JsonIgnoreProperties({"followers", "following"})
    private Set<User> followers = new HashSet<User>();
    
    public User() {}
    public User(String username, String email) { this.username = username; this.email = email; }
    
    public Long getId() {return id;}
    public String getUsername() {return username;}
    public String getEmail() {return email;}
    public Set<User> getFollowing() {return following;}
    public Set<User> getFollowers() {return followers;}
    
    public void setId(Long id) {this.id = id;}
    public void setUsername(String username) {this.username = username;}
    public void setEmail(String email) {this.email = email;}
    public void setFollowing(Set<User> following) {this.following = following;}
    public void setFollowers(Set<User> followers) {this.followers = followers;}
    
    
    
}
