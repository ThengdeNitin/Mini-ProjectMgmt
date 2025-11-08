package com.ProjectMgmt.Mini.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ProjectMgmt.Mini.Dto.ProjectRequest;
import com.ProjectMgmt.Mini.Entity.Project;
import com.ProjectMgmt.Mini.Entity.User;
import com.ProjectMgmt.Mini.Exception.ResourceNotFoundException;
import com.ProjectMgmt.Mini.Repository.ProjectRepository;

@Service
public class ProjectService {

	private ProjectRepository projectRepository;
	
	public Project createProject(ProjectRequest request, User user) {
		Project project = Project.builder()
				.name(request.getName())
				.description(request.getDescription())
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.user(user)
				.build();
				return projectRepository.save(project);
	}
	
	public List<Project> getProjectsByUser(User user) {
		return projectRepository.findByUser(user);
		}


    public Project updateProject(Long id, ProjectRequest request, User user) {
		Project project = projectRepository.findById(id)
		              .filter(p -> p.getUser().equals(user))
		              .orElseThrow(() -> new ResourceNotFoundException("Project not found for this user"));


		project.setName(request.getName());
		project.setDescription(request.getDescription());
		project.setUpdatedAt(LocalDateTime.now());
		
		return projectRepository.save(project);
	}


	public void deleteProject(Long id, User user) {
		Project project = projectRepository.findById(id)
		              .filter(p -> p.getUser().equals(user))
		              .orElseThrow(() -> new ResourceNotFoundException("Project not found for this user"));
		
		projectRepository.delete(project);
	}
}
