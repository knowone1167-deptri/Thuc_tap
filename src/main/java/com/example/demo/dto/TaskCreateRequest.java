package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateRequest {

    private String title;
    private int projectId;
    private Integer assigneeId;
}
// hoan thanh