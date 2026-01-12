package com.example.demo.service;

import com.example.demo.dto.ProjectReqest;
import com.example.demo.dto.ProjectUpdateResquest;
import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // GET ALL
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // GET BY ID
    public Project getProjectById(int id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id = " + id));
    }

    // CREATE
    public Project createProject(ProjectReqest request) {

        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException("Project name is required");
        }

        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        if (owner.getStatus() != User.Status.ACTIVE) {
            throw new RuntimeException("Owner must be ACTIVE");
        }

        Project project = new Project(
                request.getName(),
                request.getDescription(),
                owner
        );

        return projectRepository.save(project);
    }

    // UPDATE
    public Project updateProject(int id, ProjectUpdateResquest request) {

        Project project = getProjectById(id);

        if (request.getName() != null && !request.getName().isBlank()) {
            project.setName(request.getName());
        }

        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }

        if (request.getStatus() != null) {
            project.setStatus(request.getStatus());
        }

        project.setUpdatedAt(LocalDateTime.now());

        return projectRepository.save(project);
    }

    // DELETE
    public void deleteProject(int id) {

        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found");
        }

        projectRepository.deleteById(id);
    }
}
// done
