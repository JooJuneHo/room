package com.sns.room.post.repository;

import com.sns.room.post.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("SELECT p FROM Post p JOIN FETCH p.user")
    List<Post> findAllWithUser();

    Optional<Post> findById(Long id);

    List<Post> findByUserId(Long id);
}

//postDomain 추상적이다.
//PostEntity 실제 적용되는 코드
