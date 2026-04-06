package com.app.socialize.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(Long id, String content, String authorUsername, int likeCount, LocalDateTime createdAt, List<CommentResponse> comments) {

}
