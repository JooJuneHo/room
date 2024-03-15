package com.sns.room.post.repository;

import com.sns.room.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepositoryCustom {

    Page<Post> searchPostList(String search, Pageable pageable);
}
