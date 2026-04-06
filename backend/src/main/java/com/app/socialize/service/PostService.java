package com.app.socialize.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.socialize.dto.PostResponse;
import com.app.socialize.mapper.PostMapper;
import com.app.socialize.model.Post;
import com.app.socialize.model.User;
import com.app.socialize.repository.PostRepository;
import com.app.socialize.repository.UserRepository;
import com.app.socialize.util.SecurityUtils;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper; 

    public PostService(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }
    
    public List<PostResponse> getAllPosts() {
        
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                    .map(postMapper::toResponse)
                    .collect(Collectors.toList());
    }
    
    public PostResponse createPost(Post post) {
        String currentEmail = SecurityUtils.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        post.setAuthor(currentUser);  
        post.setCreatedAt(LocalDateTime.now());
        Post savedPost = postRepository.save(post);
        
        return postMapper.toResponse(savedPost);
    }
    
    public void deletePost(Long postId) {
        String currentEmail = SecurityUtils.getCurrentUserEmail();
        
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RuntimeException("Post non trouvé !"));
        
        if (!post.getAuthor().getEmail().equals(currentEmail)) {
            throw new RuntimeException("Action non autorisée !");
        }
        
        postRepository.deleteById(postId);
    }
    
    public Page<PostResponse> getFeed(int page, int size) {
        String currentEmail = SecurityUtils.getCurrentUserEmail();
        
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findFeedByUserId(currentUser.getId(), pageable);

        return posts.map(postMapper::toResponse);
    }
}