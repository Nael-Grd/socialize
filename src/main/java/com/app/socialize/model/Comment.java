package com.app.socialize.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String content;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User author;
	
	@ManyToOne
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;
	
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	public Comment() {}
	public Comment(String content, User author, Post post) {
		this.content = content;
		this.author = author;
		this.post = post;
	}
	
	public Long getId() {return id;}
	public String getContent() {return content;}
	public User getAuthor() {return author;}
	public Post getPost() {return post;}
	public LocalDateTime getCreatedAt() {return createdAt;}
	
	public void setId(Long id) {this.id = id;}
	public void setContent(String content) {this.content = content;}
	public void setAuthor(User author) {this.author =  author;}
	public void setPost(Post post) {this.post = post;}
	public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}
