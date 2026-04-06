package com.app.socialize.mapper;

import org.springframework.stereotype.Component;
import com.app.socialize.dto.CommentResponse;
import com.app.socialize.model.Comment;

@Component
public class CommentMapper {

    public CommentResponse toResponse(Comment comment) {
        if (comment == null) return null;
        
        return new CommentResponse(
            comment.getId(),
            comment.getContent(),
            comment.getAuthor().getUsername(),
            comment.getCreatedAt()
        );
    }
}