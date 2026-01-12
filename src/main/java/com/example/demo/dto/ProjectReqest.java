package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectReqest {
    private String name;
    private String description;
    private int ownerId;
}
