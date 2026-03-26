package com.app.socialize.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("post")  // Empeche la boucle infinie dans le JSON
	private List<Like> likes;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("post") 
	private List<Comment> comments;

	public Post() {}
	public Post(String content, User author) { this.content = content; this.author = author; }
	
	public Long getId() {return id;}
	public String getContent() {return content;}
	public User getAuthor() {return author;}
	public List<Like> getLikes() {return likes;} 
	public List<Comment> getComments() {return comments;} 
	
	public void setId(Long id) {this.id = id;}
	public void setContent(String content) {this.content = content;}
	public void setAuthor(User author) {this.author = author;}
	public void setLikes(List<Like> likes) {this.likes = likes;} 
	public void setComments(List<Comment> comments) {this.comments = comments;}
	
}
