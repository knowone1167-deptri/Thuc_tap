package com.example.demo.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class UserRoleId implements Serializable {

    private int userId;
    private int roleId;
}