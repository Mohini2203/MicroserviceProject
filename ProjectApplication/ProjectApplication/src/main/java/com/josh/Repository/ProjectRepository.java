package com.josh.Repository;

import com.josh.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    Project findByProjectName(String projectName);

   // Project findByProjectName(String projectName);
}
