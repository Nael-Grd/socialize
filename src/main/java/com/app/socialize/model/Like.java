package com.app.socialize.model;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "likes")
public class Like {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;
	
	public Like() {}

	public Long getId() {return id;}
	public User getUser() {return user;}
	public Post getPost() {return post;}
	
	public void setId(Long id) {this.id = id;}
	public void setUser(User user) {this.user = user;}
	public void setPost(Post post) {this.post = post;}
	
	
}
