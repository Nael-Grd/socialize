package com.app.socialize.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts")
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String content;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User author;

	public Post() {}
	public Post(String content, User author) { this.content = content; this.author = author; }
	
	public Long getId() {return id;}
	public String getContent() {return content;}
	public User getAuthor() {return author;}
	
	public void setId(Long id) {this.id = id;}
	public void setContent(String content) {this.content = content;}
	public void setAuthor(User author) {this.author = author;}
	
}
