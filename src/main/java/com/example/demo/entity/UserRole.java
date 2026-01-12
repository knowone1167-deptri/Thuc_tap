package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserRoles")
@Getter
@Setter
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "roleId")
    private Role role;

    @Column(nullable = false)
    private LocalDateTime assignedAt;
}