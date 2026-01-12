package com.example.demo.dto;

import com.example.demo.entity.Project;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProjectUpdateResquest {
    private String name;
    private String description;
    private Project.Status status;
}
