package com.sns.room.global.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("회원 인증 실패");
    }
}
