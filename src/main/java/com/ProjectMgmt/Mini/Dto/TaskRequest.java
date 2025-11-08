package com.ProjectMgmt.Mini.Dto;

import java.time.LocalDate;

import com.ProjectMgmt.Mini.Entity.Task.Priority;
import com.ProjectMgmt.Mini.Entity.Task.Status;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {

	@NotBlank(message = "Task title cannot be empty")
	private String title;
	
	private String description;
	
	private Status status;
	
    private Priority priority;
	
	private LocalDate dueDate;
	
}
