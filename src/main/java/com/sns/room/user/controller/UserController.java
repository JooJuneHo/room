package com.sns.room.user.controller;

import com.sns.room.global.jwt.UserDetailsImpl;
import com.sns.room.user.dto.LoginRequestDto;
import com.sns.room.user.dto.PasswordUpdateRequestDto;
import com.sns.room.user.dto.ResponseDto;
import com.sns.room.user.dto.SignupRequestDto;
import com.sns.room.user.dto.UserRequestDto;
import com.sns.room.user.dto.UserResponseDto;
import com.sns.room.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "User", description = "유저 컨트롤러")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입 API")
    @PostMapping("/auth/signup")
    public ResponseEntity<ResponseDto<String>> signup(
        @RequestBody SignupRequestDto signupRequestDto,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(
                ResponseDto.<String>builder()
                    .data(null).message("회원가입 실패 :" + errorMessages).build()
            );
        }

        userService.signup(signupRequestDto);

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
    }

    @Operation(summary = "로그인", description = "로그인 API")
    @PostMapping("/auth/login")
    public ResponseEntity<ResponseDto<String>> login(
        @RequestBody LoginRequestDto loginRequestDto,
        HttpServletResponse res) {

        userService.login(loginRequestDto, res);

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).build();
    }

    @Operation(summary = "유저 조회", description = "유저 조회 API")
    @GetMapping("/mypage")
    public ResponseEntity<ResponseDto<UserResponseDto>> getUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long userId = userDetails.getUser().getId();

        UserResponseDto userResponseDto = userService.getUserProfile(userId);
        return ResponseEntity.ok()
            .body(ResponseDto.<UserResponseDto>builder()
                .data(userResponseDto)
                .build());
    }

    @Operation(summary = "유저 마이페이지 수정", description = "유저 마이페이지 수정 API")
    @PutMapping("/mypage")
    public ResponseEntity<ResponseDto<UserResponseDto>> updateUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody UserRequestDto userRequestDto) {

        UserResponseDto updatedUser = userService.updateUser(userDetails, userRequestDto);
        return ResponseEntity.ok()
            .body(ResponseDto.<UserResponseDto>builder()
                .data(updatedUser)
                .build());
    }

    @Operation(summary = "유저 비밀번호 변경", description = "비밀번호 변경 API")
    @PutMapping("/password-patch")
    public ResponseEntity<ResponseDto<String>> updatePassword(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody PasswordUpdateRequestDto passwordUpdateRequestDto) {

        userService.updatePassword(userDetails, passwordUpdateRequestDto);
        return ResponseEntity.ok()
            .body(ResponseDto.<String>builder()
                .build());
    }
}
