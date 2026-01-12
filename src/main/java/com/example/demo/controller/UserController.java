package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.UserCreateRequest;
import com.example.demo.dto.UserStatusRequest;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET ALL
    @GetMapping
    public ApiResponse<List<User>> getAll() {
        return ApiResponse.success(userService.getAllUsers());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ApiResponse<User> getById(@PathVariable int id) {
        return ApiResponse.success(userService.getUserById(id));
    }

    // CREATE
    @PostMapping
    public ApiResponse<User> create(@Valid @RequestBody UserCreateRequest request) {
        return ApiResponse.success(userService.createUser(request));
    }

    // UPDATE STATUS
    @PutMapping("/{id}/status")
    public ApiResponse<User> updateStatus(@PathVariable int id,
                                          @RequestBody UserStatusRequest request) {

        return ApiResponse.success(
                userService.updateStatus(id, request.getStatus())
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable int id) {
        userService.deleteUser(id);
        return ApiResponse.success(null);
    }
}
//hoan thanh controller