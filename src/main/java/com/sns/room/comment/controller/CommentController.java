package com.sns.room.comment.controller;

import com.sns.room.comment.dto.CommentRequestDto;
import com.sns.room.comment.dto.CommentResponseDto;
import com.sns.room.comment.service.CommentService;
import com.sns.room.global.jwt.UserDetailsImpl;
import com.sns.room.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 컨트롤러")
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final NotificationService notificationService;

    @Operation(summary = "댓글 생성", description = "댓글을 작성할 수 있는 API")
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postId,
        @RequestBody CommentRequestDto commentRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto responseDto = commentService.createComment(commentRequestDto, postId,
            userDetails.getUser());
        notificationService.notifyComment(postId);
        return ResponseEntity.ok().body(responseDto);

    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정할 수 있는 API")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long postId,
        @PathVariable Long commentId,
        @RequestBody CommentRequestDto commentRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto responseDto = commentService.updateComment(commentRequestDto, postId,
            userDetails.getUser(), commentId);
        return ResponseEntity.ok().body(responseDto);
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제할 수 있는 API")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> deleteComment(@PathVariable Long postId,
        @PathVariable Long commentId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentResponseDto responseDto = commentService.deleteComment(postId,
            userDetails.getUser(), commentId);
        return ResponseEntity.ok().body(responseDto);

    }

    @Operation(summary = "댓글 조회", description = "댓글을 조회할 수 있는 API")
    @GetMapping("/search/jpa")
    public Page<CommentResponseDto> getAllComment(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "size", defaultValue = "3") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "isAsc", defaultValue = "false") boolean isAsc
    ) {
        return commentService.getAllComment(page - 1, size, sortBy, isAsc);
    }

    @GetMapping("/search/querydsl")
    public Page<CommentResponseDto> getCommentListWithPage(
        @RequestParam(name = "search", defaultValue = "") String search,
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "size", defaultValue = "3") int size,
        @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
        @RequestParam(name = "isAsc", defaultValue = "false") boolean isAsc) {
        return commentService.getCommentListWithPage(search, page - 1, size, sortBy, isAsc);
    }

    @GetMapping()
    public List<CommentResponseDto> getCommentList(
        @Nullable @RequestParam(name = "search", defaultValue = "") String search) {
        return commentService.searchCommentList(search);
    }
}
