package com.example.demo.service;

import com.example.demo.dto.UserCreateRequest;
import com.example.demo.entity.User;
import com.example.demo.expection.ApiException1;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // GET ALL
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // GET BY ID
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException1("User not found", "USER_NOT_FOUND"));
    }

    // CREATE
    public User createUser(UserCreateRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ApiException1("Username already exists", "USERNAME_EXISTS");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException1("Email already exists", "EMAIL_EXISTS");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        return userRepository.save(user);
    }

    // UPDATE STATUS
    public User updateStatus(int id, User.Status status) {

        if (status == null) {
            throw new ApiException1("Status is required", "INVALID_STATUS");
        }

        User user = getUserById(id);

        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    // DELETE
    public void deleteUser(int id) {

        if (!userRepository.existsById(id)) {
            throw new ApiException1("User not found", "USER_NOT_FOUND");
        }

        userRepository.deleteById(id);
    }
}