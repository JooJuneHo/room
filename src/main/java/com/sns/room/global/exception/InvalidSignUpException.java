package com.sns.room.global.exception;

public class InvalidSignUpException extends IllegalArgumentException{
    public InvalidSignUpException(String message){
        super(message);
    }
}
