package com.sns.room.comment.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sns.room.comment.entity.Comment;
import com.sns.room.comment.entity.QComment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QComment comment = QComment.comment;


    @Override
    public List<Comment> searchCommentList(String search) {

        return jpaQueryFactory.selectFrom(comment)
            .where(comment.content.like("%" + search + "%"))
            .fetch();
    }


    @Override
    public Page<Comment> getCommentListWithPage(String search, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(comment.content.contains(search));

        Sort sort = Sort.by(Direction.DESC, "createdAt");

        List<Comment> comments;

        if (pageable.getSort().equals(sort)) {
            comments = jpaQueryFactory.selectFrom(comment)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.createdAt.desc())
                .fetch();
        } else {
            comments = jpaQueryFactory.selectFrom(comment)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.createdAt.asc())
                .fetch();
        }

        long totalCount = jpaQueryFactory.selectFrom(comment)
            .where(predicate)
            .fetch().size();

        return new PageImpl<>(comments, pageable, totalCount);
    }
}
