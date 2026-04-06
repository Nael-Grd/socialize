package com.app.socialize.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.app.socialize.dto.PostResponse;
import com.app.socialize.model.Post;

@Component
public class PostMapper {

    private final CommentMapper commentMapper;

    public PostMapper(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public PostResponse toResponse(Post post) {
        return new PostResponse(
            post.getId(),
            post.getContent(),
            post.getAuthor().getUsername(),
            post.getLikes() != null ? post.getLikes().size() : 0,
            post.getCreatedAt(),
            post.getComments() == null ? List.of() :     // On transforme la liste de commentaires en utilisant le CommentMapper
                post.getComments().stream()
                    .map(commentMapper::toResponse)
                    .collect(Collectors.toList())
        );
    }
}