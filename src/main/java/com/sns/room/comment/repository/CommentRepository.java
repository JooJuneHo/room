package com.sns.room.comment.repository;

import com.sns.room.comment.entity.Comment;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    Optional<Comment> findFirstByPostIdOrderByCreatedAtDesc(Long id);

    Page<Comment> findAllByOrderByModifiedAtDesc(Pageable pageable);
}
