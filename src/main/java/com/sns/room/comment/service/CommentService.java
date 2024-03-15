package com.sns.room.comment.service;

import com.sns.room.comment.dto.CommentRequestDto;
import com.sns.room.comment.dto.CommentResponseDto;
import com.sns.room.comment.entity.Comment;
import com.sns.room.comment.repository.CommentRepository;
import com.sns.room.global.exception.InvalidCommentException;
import com.sns.room.global.exception.InvalidPostException;
import com.sns.room.global.exception.InvalidUserException;
import com.sns.room.post.entity.Post;
import com.sns.room.post.repository.PostRepository;
import com.sns.room.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, long postId,
        User user) {
        Post post = checkPost(postId);
        Comment comment = new Comment(commentRequestDto.getContent(), post, user);

        commentRepository.save(comment);

        return new CommentResponseDto(comment.getPost().getTitle(), comment.getUser().getUsername(),
            comment.getContent(), comment.getCreatedAt(), comment.getModifiedAt());
    }

    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto commentRequestDto, long postId,
        User user, long commentId) {
        Post post = checkPost(postId);
        Comment comment = checkComment(commentId);

        isValidPost(post, comment);
        isValidUser(user, comment);

        comment.Update(commentRequestDto.getContent());

        return new CommentResponseDto(comment.getPost().getTitle(), comment.getUser().getUsername(),
            comment.getContent(), comment.getCreatedAt(), comment.getModifiedAt());
    }


    @Transactional
    public CommentResponseDto deleteComment(long postId, User user, long commentId) {
        Post post = checkPost(postId);
        Comment comment = checkComment(commentId);

        isValidPost(post, comment);
        isValidUser(user, comment);

        comment.SoftDeleted();

        return new CommentResponseDto(comment.getPost().getTitle(), comment.getUser().getUsername(),
            comment.getContent(), comment.getCreatedAt(), comment.getModifiedAt());
    }

    public Page<CommentResponseDto> getAllComment(int page, int size, String sortBy,
        boolean isAsc) {
        Direction direction = isAsc ? Direction.ASC : Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Comment> commentPage = commentRepository.findAll(pageable);

        return commentPage.map(CommentResponseDto::new);
    }

    public List<CommentResponseDto> searchCommentList(String search) {
        // queryDSL를 이용한 where = category 조회 로직 실습

        //1. search가 없을 때는 전체 조회
        //2. search가 있을 때는 해당 카테고리만 조회

        List<Comment> response = commentRepository.searchCommentList(search);

        return response.stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

    public Page<CommentResponseDto> getCommentListWithPage(String search, int page, int size,
        String sortBy, boolean isAsc) {
        Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Comment> commentPage = commentRepository.getCommentListWithPage(search, pageable);

        return commentPage.map(CommentResponseDto::new);
    }

    // 존재하는 게시글인지 검증
    private Post checkPost(long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new InvalidPostException("존재하지 않는 게시글입니다."));

        return post;
    }


    // 존재하는 댓글인지 검증
    private Comment checkComment(long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
            InvalidCommentException::new);
        return comment;
    }

    // 댓글이 달려있는 게시글인지 검증
    private boolean isValidPost(Post post, Comment comment) {
        if (!post.getId().equals(comment.getPost().getId())) {
            throw new InvalidPostException("올바르지 않은 게시글입니다.");
        }
        return true;
    }

    // 댓글을 수정/삭제할 권한이 있는 유저인지 검증
    private boolean isValidUser(User user, Comment comment) {
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new InvalidUserException("작성자만 수정/삭제가능합니다.");
        }
        return true;
    }

    public Comment findLatestComment(Long postId) {
        return commentRepository.findFirstByPostIdOrderByCreatedAtDesc(
            postId).orElseThrow(
            () -> new IllegalArgumentException("댓글을 찾을 수 없습니다.")
        );
    }


}
