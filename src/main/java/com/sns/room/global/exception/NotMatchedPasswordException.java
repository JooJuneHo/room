package com.sns.room.global.exception;

import org.springframework.security.authentication.BadCredentialsException;

public class NotMatchedPasswordException extends BadCredentialsException {


    public NotMatchedPasswordException(String message) {
        super(message);
    }
}
