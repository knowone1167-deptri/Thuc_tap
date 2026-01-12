package com.example.demo.dto;

import com.example.demo.entity.User;

public class UserStatusRequest {
    private User.Status status;

    public User.Status getStatus() {
        return status;
    }

    public void setStatus(User.Status status) {
        this.status = status;
    }
}
