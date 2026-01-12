package com.example.demo.dto;

import com.example.demo.entity.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StatusRequest {
    private Task.Status status;
}
