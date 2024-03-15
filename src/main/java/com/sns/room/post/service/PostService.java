package com.sns.room.post.service;

import com.sns.room.global.exception.InvalidUserException;
import com.sns.room.global.exception.NotFoundPostException;
import com.sns.room.post.dto.PostRequestDto;
import com.sns.room.post.dto.PostResponseDto;
import com.sns.room.post.entity.Post;
import com.sns.room.post.repository.PostRepository;
import com.sns.room.user.entity.User;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    //4레이어드로 수정해보기
    private final PostRepository postRepository;

    //게시글 생성
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {

        //게시글 객체 생성
        Post savePost = new Post(requestDto, user);
        //post 저장
        postRepository.save(savePost);
        //dto 반환
        return new PostResponseDto(savePost);
    }

    //게시글 전체 조회(검색 기능 추가)
    public Page<PostResponseDto> findAllPost(String search, int page, int size, String sortBy,
        boolean isAsc) {
        Direction direction = isAsc ? Direction.ASC : Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> posts = postRepository.searchPostList(search, pageable);

        return posts.map(PostResponseDto::new);
    }

    //게시글 선택 조회pull
    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new NotFoundPostException(postId));
        return new PostResponseDto(post);
    }

    //게시글 삭제
    @Transactional
    public void delete(Long postId, User user) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new NotFoundPostException(postId));
        if (user.getId().equals(post.getUser().getId())) {
            postRepository.delete(post);
        } else {
            throw new InvalidUserException("삭제 권한이 없습니다.");
        }
    }

    //게시글 업데이트
    @Transactional
    public ResponseEntity<PostResponseDto> updatePost(Long postId, PostRequestDto requestDto,
        User user) {
        Post updatePost = postRepository.findById(postId)
            .orElseThrow(() -> new NotFoundPostException(postId));
        Long updatePostId = updatePost.getUser().getId();
        if (!updatePostId.equals(user.getId())) {
            throw new InvalidUserException("수정 권한이 없습니다.");
        } else {
            updatePost.updatePost(requestDto.getTitle(), requestDto.getContent());
        }
        return ResponseEntity.ok(new PostResponseDto(updatePost));
    }

    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new NotFoundPostException(postId));
    }

    public List<Post> findByUserId(Long id) {
        return postRepository.findByUserId(id);
    }
}

