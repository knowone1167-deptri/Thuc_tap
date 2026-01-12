package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.UserCreateRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.entity.UserRoleId;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       UserRoleRepository userRoleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // =========================
    // REGISTER
    // =========================
    public User register(UserCreateRequest request) {

        // 1️⃣ Tạo User
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );

        user = userRepository.save(user);


        Role role = roleRepository
                .findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));


        UserRole userRole = new UserRole();

        UserRoleId id = new UserRoleId();
        id.setUserId(user.getId());
        id.setRoleId(role.getId());

        userRole.setId(id);
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setAssignedAt(LocalDateTime.now());

        userRoleRepository.save(userRole);

        return user;
    }

    // =========================
    // LOGIN
    // =========================
    public String login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Lấy role từ bảng trung gian
        UserRole userRole = userRoleRepository
                .findFirstByUser(user)
                .orElseThrow(() -> new RuntimeException("User has no role"));

        String roleName = userRole.getRole().getName();

        return jwtUtil.generateToken(user.getEmail(), roleName);
    }
}
//hoan thanh auth service