package com.ProjectMgmt.Mini.Controller;

import com.ProjectMgmt.Mini.Dto.TaskRequest;
import com.ProjectMgmt.Mini.Entity.Project;
import com.ProjectMgmt.Mini.Entity.Task;
import com.ProjectMgmt.Mini.Entity.User;
import com.ProjectMgmt.Mini.Repository.ProjectRepository;
import com.ProjectMgmt.Mini.Repository.TaskRepository;
import com.ProjectMgmt.Mini.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{projectId}")
    public Task createTask(@PathVariable Long projectId,
                           @Valid @RequestBody TaskRequest request,
                           Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(projectId)
                .filter(p -> p.getUser().equals(user))
                .orElseThrow(() -> new RuntimeException("Project not found or access denied"));

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .project(project)
                .build();

        return taskRepository.save(task);
    }

    @GetMapping("/project/{projectId}")
    public List<Task> getTasksByProject(@PathVariable Long projectId, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(projectId)
                .filter(p -> p.getUser().equals(user))
                .orElseThrow(() -> new RuntimeException("Project not found or access denied"));

        return taskRepository.findByProject(project);
    }

    @PutMapping("/{taskId}")
    public Task updateTask(@PathVariable Long taskId,
                           @Valid @RequestBody TaskRequest request,
                           Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskRepository.findById(taskId)
                .filter(t -> t.getProject().getUser().equals(user))
                .orElseThrow(() -> new RuntimeException("Task not found or access denied"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    @DeleteMapping("/{taskId}")
    public String deleteTask(@PathVariable Long taskId, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskRepository.findById(taskId)
                .filter(t -> t.getProject().getUser().equals(user))
                .orElseThrow(() -> new RuntimeException("Task not found or access denied"));

        taskRepository.delete(task);
        return "Task deleted successfully";
    }

    @GetMapping("/search")
    public List<Task> searchTasks(@RequestParam String query, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Project> projects = projectRepository.findByUser(user);

        return projects.stream()
                .flatMap(p -> taskRepository.findByProject(p).stream())
                .filter(t -> t.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                             t.getDescription().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    @GetMapping("/sort")
    public List<Task> sortTasks(@RequestParam(defaultValue = "dueDate") String sortBy,
                                Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Project> projects = projectRepository.findByUser(user);

        List<Task> tasks = projects.stream()
                .flatMap(p -> taskRepository.findByProject(p).stream())
                .collect(Collectors.toList());

        Comparator<Task> comparator;

        switch (sortBy.toLowerCase()) {
            case "priority":
                comparator = Comparator.comparing(Task::getPriority);
                break;
            case "status":
                comparator = Comparator.comparing(Task::getStatus);
                break;
            case "duedate":
            default:
                comparator = Comparator.comparing(
                        Task::getDueDate,
                        Comparator.nullsLast(Comparator.naturalOrder())
                );
                break;
        }

        return tasks.stream().sorted(comparator).collect(Collectors.toList());
    }
}
