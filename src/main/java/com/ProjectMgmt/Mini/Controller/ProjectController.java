package com.ProjectMgmt.Mini.Controller;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ProjectMgmt.Mini.Dto.ProjectRequest;
import com.ProjectMgmt.Mini.Entity.Project;
import com.ProjectMgmt.Mini.Entity.User;
import com.ProjectMgmt.Mini.Repository.ProjectRepository;
import com.ProjectMgmt.Mini.Repository.UserRepository;

@RestController
@RequestMapping("/api/projects") 
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public Project createProject(@Valid @RequestBody ProjectRequest request, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build();

        return projectRepository.save(project);
    }

    @GetMapping
    public List<Project> getAllProjects(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return projectRepository.findByUser(user);
    }

    @PutMapping("/{id}")
    public Project updateProject(@PathVariable Long id,
                                 @Valid @RequestBody ProjectRequest request,
                                 Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(id)
                .filter(p -> p.getUser().equals(user))
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setUpdatedAt(LocalDateTime.now());

        return projectRepository.save(project);
    }

    @DeleteMapping("/{id}")
    public String deleteProject(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(id)
                .filter(p -> p.getUser().equals(user))
                .orElseThrow(() -> new RuntimeException("Project not found"));

        projectRepository.delete(project);
        return "Project deleted successfully";
    }
}
