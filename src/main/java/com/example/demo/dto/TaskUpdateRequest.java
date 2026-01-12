package com.example.demo.dto;

import com.example.demo.entity.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TaskUpdateRequest {
    private String title;
    private String description;
    private Task.Status status;

    public TaskUpdateRequest() {}

    public TaskUpdateRequest(String title, String description, Task.Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
