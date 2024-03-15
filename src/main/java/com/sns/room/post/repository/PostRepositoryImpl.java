package com.sns.room.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sns.room.post.entity.Post;
import com.sns.room.post.entity.QPost;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<Post> searchPostList(String search, Pageable pageable) {
        QPost post = QPost.post;

        BooleanBuilder predicate = new BooleanBuilder();

        predicate.or(post.title.contains(search));
        predicate.or(post.content.contains(search));
        predicate.or(post.user.username.contains(search));

        List<Post> posts = jpaQueryFactory.selectFrom(post)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = jpaQueryFactory.selectFrom(post)
            .where(predicate)
            .fetch().size();

        return new PageImpl<>(posts, pageable, totalCount);
    }
}
