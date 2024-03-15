package com.sns.room.comment.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentRequestDtoTest {

    @Test
    @DisplayName("댓글 요청 Dto 생성 성공")
    void createCommentRequestDto_Test() {
        //given
        String comment = "댓글 생성";
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setContent(comment);

        //then
        assertEquals(commentRequestDto.getContent(), "댓글 생성");
    }

}