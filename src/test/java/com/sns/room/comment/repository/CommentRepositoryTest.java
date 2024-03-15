package com.sns.room.comment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sns.room.comment.entity.Comment;
import com.sns.room.post.dto.PostRequestDto;
import com.sns.room.post.entity.Post;
import com.sns.room.user.entity.User;
import com.sns.room.user.entity.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
public class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @MockBean
    JPAQueryFactory jpaQueryFactory;

    @Test
    @DisplayName("댓글 저장 테스트")
    void save() {
        User user = new User("username", "email", "password", UserRoleEnum.USER);
        Post post = new Post(new PostRequestDto("title", "content", "photo"),
            user);

        Comment comment = new Comment("comment", post, user);

        Comment savedComment = commentRepository.save(comment);

        assertEquals(comment.getCommentId(), savedComment.getCommentId());
    }

    @Test
    @DisplayName("댓글 조회 테스트")
    void find() {
        User user = new User("username", "email", "password", UserRoleEnum.USER);
        Post post = new Post(new PostRequestDto("title", "content", "photo"),
            user);

        Comment comment = new Comment("comment", post, user);

        commentRepository.save(comment);

        Comment savedComment = commentRepository.findById(comment.getCommentId()).orElseThrow(null);

        assertEquals(comment.getCommentId(), savedComment.getCommentId());
    }


}
