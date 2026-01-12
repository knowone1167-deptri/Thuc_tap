package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ProjectReqest;
import com.example.demo.dto.ProjectUpdateResquest;
import com.example.demo.entity.Project;
import com.example.demo.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // GET ALL
    @GetMapping
    public ApiResponse<List<Project>> getAllProjects() {
        return ApiResponse.success(projectService.getAllProjects());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ApiResponse<Project> getProject(@PathVariable int id) {
        return ApiResponse.success(projectService.getProjectById(id));
    }

    // CREATE
    @PostMapping
    public ApiResponse<Project> createProject(@RequestBody ProjectReqest request) {
        return ApiResponse.success(
                projectService.createProject(request)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ApiResponse<Project> updateProject(@PathVariable int id,
                                              @RequestBody ProjectUpdateResquest request) {
        return ApiResponse.success(
                projectService.updateProject(id, request)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProject(@PathVariable int id) {
        projectService.deleteProject(id);
        return ApiResponse.success(null);
    }
}