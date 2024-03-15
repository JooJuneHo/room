package com.sns.room.comment.repository;

import com.sns.room.comment.entity.Comment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepositoryCustom {

    List<Comment> searchCommentList(String search);

    Page<Comment> getCommentListWithPage(String search, Pageable pageable);
}
