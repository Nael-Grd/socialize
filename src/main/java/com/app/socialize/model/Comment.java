package com.app.socialize.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	private User author;
	
	@ManyToOne
	private Post post;
	
	public Comment() {}
	public Comment(String content, User author, Post post) {
		this.content = content;
		this.author = author;
		this.post = post;
	}
	
	public Long getId() {return id;}
	public String getcontent() {return content;}
	public User getAuthor() {return author;}
	public Post getPost() {return post;}
	
	public void setId(Long id) {this.id = id;}
	public void setcontent(String content) {this.content = content;}
	public void setAuthor(User author) {this.author =  author;}
	public void setPost(Post post) {this.post = post;}
}
