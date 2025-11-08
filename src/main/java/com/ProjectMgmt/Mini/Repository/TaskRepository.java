package com.ProjectMgmt.Mini.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ProjectMgmt.Mini.Entity.Project;
import com.ProjectMgmt.Mini.Entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

	List<Task> findByProject(Project project);
}
