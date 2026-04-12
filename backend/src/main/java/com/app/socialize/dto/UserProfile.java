package com.app.socialize.dto;

public record UserProfile(Long id, String username, int followersCount, int followingCount, boolean isFollowedByCurrentUser) {

}
