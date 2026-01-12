package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {

    @NotBlank(message = "Username must not be empty")
    @Size(min = 3, max = 50, message = "Username length must be 3–50")
    private String username;

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;


    @NotBlank
    private String role;

}
