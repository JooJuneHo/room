package com.sns.room.comment.dto;

import com.sns.room.comment.entity.Comment;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private String postTitle;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public CommentResponseDto(String postTitle, String username, String content,
        LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.postTitle = postTitle;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public CommentResponseDto(Comment content) {
        this.postTitle = content.getPost().getTitle();
        this.username = content.getUser().getUsername();
        this.content = content.getContent();
        this.createdAt = content.getCreatedAt();
        this.modifiedAt = content.getModifiedAt();
    }

}
