package com.sns.room.global.exception;

import com.sns.room.global.exception.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
        MethodArgumentNotValidException e) {
        log.error("회원 검증 실패", e);
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(message);
        return ResponseEntity.badRequest().body(errorResponseDto);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException e) {
        log.error("인증 실패", e);
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidInputException(InvalidInputException e) {
        log.error("잘못된 입력", e);
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleUnhandledException(RuntimeException e) {
        log.error("처리되지 않은 예외 발생", e);
        return ResponseEntity.badRequest().body("Unhandled Exception");
    }

    @ExceptionHandler(InvalidCommentException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCommentException(InvalidCommentException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundUserException(NotFoundUserException e){
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(NotFoundPostException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundPostException(NotFoundPostException e){
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(InvalidSignUpException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidSignUpException(InvalidSignUpException e){
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }

    @ExceptionHandler(NotMatchedPasswordException.class)
    public ResponseEntity<ErrorResponseDto> handleNotMatchedPasswordException(NotMatchedPasswordException e){
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }
}
