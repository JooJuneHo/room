package com.sns.room.global.exception;

import java.util.NoSuchElementException;

public class NotFoundPostException extends NoSuchElementException {

    public NotFoundPostException(Long postId){
        super("postId = " + postId + "와 일치하는 게시글을 찾을 수 없습니다.");
    }
}
