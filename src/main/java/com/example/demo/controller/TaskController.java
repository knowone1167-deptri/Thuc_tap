package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // GET ALL
    @GetMapping
    public ApiResponse<List<Task>> getAll() {
        return ApiResponse.success(taskService.getAllTasks());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ApiResponse<Task> getById(@PathVariable int id) {
        return ApiResponse.success(taskService.getTaskById(id));
    }

    // CREATE
    @PostMapping
    public ApiResponse<Task> create(@RequestBody TaskCreateRequest request) {
        return ApiResponse.success(
                taskService.createTask(request)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ApiResponse<Task> update(@PathVariable int id,
                                    @RequestBody TaskUpdateRequest request) {
        return ApiResponse.success(
                taskService.updateTask(id, request)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable int id) {
        taskService.deleteTask(id);
        return ApiResponse.success(null);
    }

    // FILTER USER
    @GetMapping("/user/{userId}")
    public ApiResponse<List<Task>> getByUser(@PathVariable int userId) {
        return ApiResponse.success(taskService.getTasksByUser(userId));
    }

    // FILTER PROJECT
    @GetMapping("/project/{projectId}")
    public ApiResponse<List<Task>> getByProject(@PathVariable int projectId) {
        return ApiResponse.success(taskService.getTasksByProject(projectId));
    }

    // ASSIGN
    @PutMapping("/{id}/assign")
    public ApiResponse<Task> assign(@PathVariable int id,
                                    @RequestBody AssignRequest request) {
        return ApiResponse.success(
                taskService.assignTask(id, request.getUserId())
        );
    }

    // UPDATE STATUS
    @PutMapping("/{id}/status")
    public ApiResponse<Task> updateStatus(@PathVariable int id,
                                          @RequestBody StatusRequest request) {
        return ApiResponse.success(
                taskService.updateTask(id, new TaskUpdateRequest(null, null, request.getStatus()))
        );
    }
}