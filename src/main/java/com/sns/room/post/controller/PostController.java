package com.sns.room.post.controller;

import com.sns.room.global.jwt.UserDetailsImpl;
import com.sns.room.post.dto.PostRequestDto;
import com.sns.room.post.dto.PostResponseDto;
import com.sns.room.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 컨트롤러")
public class PostController {

    private final PostService postService;

    //게시글 등록
    @Operation(summary = "게시글 생성", description = "게시글 생성 API")
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> createPost(
        @RequestBody PostRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok(postService.createPost(requestDto, userDetails.getUser()));
    }

    //게시글 전체 조회
    @Operation(summary = "게시글 전체조회", description = "게시글 전체조회 API")
    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDto>> getAllPost(
        @RequestParam(name = "search", defaultValue = "") String search,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "size", defaultValue = "3") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "isAsc", defaultValue = "true") boolean isAsc) {
        Page<PostResponseDto> postList = postService.findAllPost(search, page - 1, size, sortBy,
            isAsc);
        return ResponseEntity.ok(postList);
    }

    //게시글 선택 조회
    @Operation(summary = "게시글 선택조회", description = "게시글 선택조회 API")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(postService.getPost(postId));
    }

    //게시글 삭제
    @Operation(summary = "게시글 삭제", description = "게시글 삭제 API")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            postService.delete(postId, userDetails.getUser());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //게시글 수정
    @Operation(summary = "게시글 수정", description = "게시글 수정 API")
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId,
        @RequestBody PostRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        return postService.updatePost(postId, requestDto, userDetails.getUser());
    }
}
