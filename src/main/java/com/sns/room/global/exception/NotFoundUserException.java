package com.sns.room.global.exception;

import java.util.NoSuchElementException;

public class NotFoundUserException extends NoSuchElementException {

    public NotFoundUserException(Long userId){
        super("userId = " + userId + " 와 일치하는 회원 정보가 없습니다.");
    }

    public NotFoundUserException(String message){
        super(message);
    }
}
