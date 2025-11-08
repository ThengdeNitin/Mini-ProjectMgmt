package com.ProjectMgmt.Mini.Service;

import com.ProjectMgmt.Mini.Dto.TaskRequest;
import com.ProjectMgmt.Mini.Entity.Project;
import com.ProjectMgmt.Mini.Entity.Task;
import com.ProjectMgmt.Mini.Entity.User;
import com.ProjectMgmt.Mini.Exception.ResourceNotFoundException;
import com.ProjectMgmt.Mini.Repository.ProjectRepository;
import com.ProjectMgmt.Mini.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {


     @Autowired
     private TaskRepository taskRepository;


     @Autowired
     private ProjectRepository projectRepository;
     
     public Task createTask(Long projectId, TaskRequest request, User user) {
    	 Project project = projectRepository.findById(projectId)
    	 .filter(p -> p.getUser().equals(user))
    	 .orElseThrow(() -> new ResourceNotFoundException("Project not found for this user"));


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
     
    public List<Task> getTasksByProject(Long projectId, User user) {
    	 Project project = projectRepository.findById(projectId)
    	 .filter(p -> p.getUser().equals(user))
    	 .orElseThrow(() -> new ResourceNotFoundException("Project not found for this user"));
    	 return taskRepository.findByProject(project);
    }


    public Task updateTask(Long taskId, TaskRequest request, User user) {
    	 Task task = taskRepository.findById(taskId)
    	 .filter(t -> t.getProject().getUser().equals(user))
    	 .orElseThrow(() -> new ResourceNotFoundException("Task not found for this user"));


    	 task.setTitle(request.getTitle());
    	 task.setDescription(request.getDescription());
    	 task.setStatus(request.getStatus());
    	 task.setPriority(request.getPriority());
    	 task.setDueDate(request.getDueDate());
    	 task.setUpdatedAt(LocalDateTime.now());


    	 return taskRepository.save(task);
    }
    	 
    public void deleteTask(Long taskId, User user) {
    	Task task = taskRepository.findById(taskId)
    		 .filter(t -> t.getProject().getUser().equals(user))
    		 .orElseThrow(() -> new ResourceNotFoundException("Task not found for this user"));
    		 taskRepository.delete(task);
    }


    public List<Task> searchTasks(String query, User user) {
    		 List<Project> projects = projectRepository.findByUser(user);


    	return projects.stream()
    		.flatMap(p -> taskRepository.findByProject(p).stream())
    		.filter(t -> t.getTitle().toLowerCase().contains(query.toLowerCase()) ||
    		t.getDescription().toLowerCase().contains(query.toLowerCase()))
    		.collect(Collectors.toList());
    }


    public List<Task> sortTasks(String sortBy, User user) {
    	List<Project> projects = projectRepository.findByUser(user);
    	List<Task> tasks = projects.stream()
    		.flatMap(p -> taskRepository.findByProject(p).stream())
    		.collect(Collectors.toList());


        if ("dueDate".equalsIgnoreCase(sortBy)) {
    	    return tasks.stream().sorted(Comparator.comparing(Task::getDueDate)).collect(Collectors.toList());
        } else if ("priority".equalsIgnoreCase(sortBy)) {
        	
         return tasks.stream().sorted(Comparator.comparing(Task::getPriority)).collect(Collectors.toList());
        }
        
        return tasks;
    }
  }