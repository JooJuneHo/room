package com.sns.room.comment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.sns.room.comment.dto.CommentRequestDto;
import com.sns.room.comment.dto.CommentResponseDto;
import com.sns.room.comment.entity.Comment;
import com.sns.room.comment.repository.CommentRepository;
import com.sns.room.comment.test.PostTest;
import com.sns.room.post.dto.PostRequestDto;
import com.sns.room.post.entity.Post;
import com.sns.room.post.repository.PostRepository;
import com.sns.room.user.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest extends PostTest {

    @InjectMocks
    CommentService commentService;
    @Mock
    CommentRepository commentRepository;
    @Mock
    PostRepository postRepository;


    @Test
    @DisplayName("댓글 생성")
    void createComment() {
        //given
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("comment");

        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        Post post = new Post(new PostRequestDto("title", "content", "photo"),
            user);
        ReflectionTestUtils.setField(post, "id", 1L);
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(post));

        Comment comment = new Comment(requestDto.getContent(), post, user);

        //when
        CommentResponseDto commentResponseDto = commentService.createComment(requestDto,
            TEST_POST_ID, TEST_USER);

        //then
        assertEquals(commentResponseDto.getContent(), comment.getContent());
        assertEquals(comment.getUser(), user);
        assertEquals(comment.getPost(), post);
    }

    @Test
    @DisplayName("댓글 수정")
    void updateComment() {
        //given
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("comment");

        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        Post post = new Post(new PostRequestDto("title", "content", "photo"),
            user);

        Comment comment = new Comment(requestDto.getContent(), post, user);
        ReflectionTestUtils.setField(comment, "commentId", 1L);

        ReflectionTestUtils.setField(post, "id", 1L);
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(post));
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        //when
        CommentResponseDto commentResponseDto = commentService.updateComment(requestDto,
            TEST_POST_ID, user, comment.getCommentId());

        //then
        assertEquals(commentResponseDto.getContent(), comment.getContent());
        assertEquals(comment.getUser(), user);
        assertEquals(comment.getPost(), post);
    }

    @Test
    void deleteComment() {
        //given
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("comment");

        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        Post post = new Post(new PostRequestDto("title", "content", "photo"),
            user);

        Comment comment = new Comment(requestDto.getContent(), post, user);
        ReflectionTestUtils.setField(comment, "commentId", 1L);

        ReflectionTestUtils.setField(post, "id", 1L);
        given(postRepository.findById(TEST_POST_ID)).willReturn(Optional.of(post));
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        //when
        CommentResponseDto commentResponseDto = commentService.deleteComment(TEST_POST_ID, user,
            comment.getCommentId());

        //then
        assertEquals(commentResponseDto.getContent(), comment.getContent());
        assertEquals(comment.getUser(), user);
        assertEquals(comment.getPost(), post);
        assertEquals(comment.getCreatedAt(), commentResponseDto.getCreatedAt());
        assertEquals(comment.getModifiedAt(), commentResponseDto.getModifiedAt());
    }
}
