package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.UserCreateRequest;
import com.example.demo.entity.User;
import com.example.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Xác thực người dùng",
        description = "API đăng ký và đăng nhập tài khoản"
)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Đăng ký tài khoản",
            description = "Tạo tài khoản người dùng mới trong hệ thống"
    )
    @PostMapping("/register")
    public User register(@Valid @RequestBody UserCreateRequest request) {
        return authService.register(request);
    }

    @Operation(
            summary = "Đăng nhập",
            description = "Xác thực tài khoản và trả về token đăng nhập"
    )
    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
