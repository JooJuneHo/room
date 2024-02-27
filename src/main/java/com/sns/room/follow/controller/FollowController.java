package com.sns.room.follow.controller;

import com.sns.room.follow.dto.FollowResponseDto;
import com.sns.room.follow.dto.FollowerResponseDto;
import com.sns.room.follow.dto.FollowingResponseDto;
import com.sns.room.follow.service.FollowService;
import com.sns.room.global.jwt.UserDetailsImpl;
import com.sns.room.notification.service.NotificationService;
import com.sns.room.post.dto.PostResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowService followService;
    private final NotificationService notificationService;

    @PostMapping("/follows/{toUserId}")
    public ResponseEntity<FollowResponseDto> createFollow(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long toUserId) {
        followService.createFollow(userDetails.getUser(), toUserId);
        notificationService.notifyFollow(toUserId);
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(FollowResponseDto.builder().message("팔로우 성공하였습니다.").build());
    }

    @DeleteMapping("/follows/{toUserId}")
    public ResponseEntity<FollowResponseDto> deleteFollow(
        @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long toUserId) {
        followService.deleteFollow(userDetails.getUser(), toUserId);
        return ResponseEntity.status(HttpStatus.OK.value())
            .body(FollowResponseDto.builder().message("팔로우 취소되었습니다.").build());
    }

    @GetMapping("/users/{userId}/follows/following")
    public List<FollowingResponseDto> getFollowingList(
        @PathVariable Long userId) {
        List<FollowingResponseDto> followingResponseDtos =
            followService.getFollowingList(userId);

        return ResponseEntity.status(HttpStatus.OK.value()).body(followingResponseDtos).getBody();
    }

    @GetMapping("/users/{userId}/follows/following")
    public List<FollowerResponseDto> getFollowerList(
        @PathVariable Long userId) {
        List<FollowerResponseDto> followerResponseDtos =
            followService.getFollowerList(userId);

        return ResponseEntity.status(HttpStatus.OK.value()).body(followerResponseDtos).getBody();
    }

    @GetMapping("/follows/todo")
    public ResponseEntity<List<PostResponseDto>> getAllFollowingPost(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<PostResponseDto> postResponseDtos =
            followService.getAllFollowingPost(userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK.value()).body(postResponseDtos);
    }
}
