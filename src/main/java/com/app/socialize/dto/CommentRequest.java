package com.app.socialize.dto;

public record CommentRequest(String content, Long userId, Long postId) {

}
