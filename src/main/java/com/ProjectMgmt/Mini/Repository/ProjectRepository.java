package com.ProjectMgmt.Mini.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ProjectMgmt.Mini.Entity.Project;
import com.ProjectMgmt.Mini.Entity.User;

public interface ProjectRepository extends JpaRepository<Project, Long>{

	List<Project> findByUser(User user);
}
