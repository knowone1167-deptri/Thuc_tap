package com.example.demo.service;

import com.example.demo.dto.TaskCreateRequest;
import com.example.demo.dto.TaskUpdateRequest;
import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // =========================
    // PRIVATE HELPERS
    // =========================

    private Task findTask(int id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    private Project findProject(int id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    private User findUser(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void checkUserActive(User user) {
        if (user.getStatus() != User.Status.ACTIVE) {
            throw new RuntimeException("User is inactive");
        }
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private boolean isManager(Authentication auth) {
        return auth.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
    }

    // =========================
    // GET ALL
    // =========================

    public List<Task> getAllTasks() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = getCurrentUser();

        if (isManager(auth)) {
            return taskRepository.findAll();
        }

        return taskRepository.findByAssignee(currentUser);
    }

    // =========================
    // GET BY ID
    // =========================

    public Task getTaskById(int id) {

        Task task = findTask(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (isManager(auth)) {
            return task;
        }

        String email = auth.getName();

        if (task.getAssignee() == null ||
                !task.getAssignee().getEmail().equals(email)) {
            throw new RuntimeException("Access denied");
        }

        return task;
    }

    // =========================
    // CREATE
    // =========================

    public Task createTask(TaskCreateRequest request) {

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new RuntimeException("Title must not be empty");
        }

        Project project = findProject(request.getProjectId());

        if (taskRepository.existsByTitleAndProject_Id(
                request.getTitle(), request.getProjectId())) {
            throw new RuntimeException("Task title already exists in this project");
        }

        Task task = new Task(request.getTitle(), project);

        if (request.getAssigneeId() != null) {
            User user = findUser(request.getAssigneeId());
            checkUserActive(user);
            task.assign(user);
        }

        return taskRepository.save(task);
    }

    // =========================
    // UPDATE
    // =========================

    // UPDATE
    public Task updateTask(int id, TaskUpdateRequest request) {

        Task task = getTaskById(id);

        if (task.getStatus() == Task.Status.DONE) {
            throw new RuntimeException("Cannot update a completed task");
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {

            if (!request.getTitle().equals(task.getTitle())
                    && taskRepository.existsByTitleAndProject_Id(
                    request.getTitle(), task.getProject().getId())) {

                throw new RuntimeException("Task title already exists in this project");
            }

            task.setTitle(request.getTitle());
        }

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    // =========================
    // DELETE
    // =========================

    public void deleteTask(int id) {
        taskRepository.deleteById(id);
    }

    // =========================
    // FILTER
    // =========================

    public List<Task> getTasksByUser(int userId) {

        User user = findUser(userId);
        return taskRepository.findByAssignee_Id(user.getId());
    }

    public List<Task> getTasksByProject(int projectId) {

        findProject(projectId);
        return taskRepository.findByProject_Id(projectId);
    }

    // =========================
    // ASSIGN
    // =========================

    public Task assignTask(int taskId, int userId) {

        Task task = findTask(taskId);
        User user = findUser(userId);

        checkUserActive(user);

        if (task.getProject().getOwner().getId() != user.getId()) {
            throw new RuntimeException("User does not belong to this project");
        }

        task.assign(user);

        return taskRepository.save(task);
    }
}
//hoan thanh task