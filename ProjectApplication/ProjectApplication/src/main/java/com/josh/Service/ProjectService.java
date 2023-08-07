package com.josh.Service;

import com.josh.Entity.Project;

import java.util.List;

public interface ProjectService {
    Project getProjectByName(String projectName);

    List<Project>getAllProject();
    Project getProject(Long projectId );

    Project addProject(Project project);

    Project updateProject(Project project,Long projectId);

    void deleteProject(Long projectId);

   // boolean addEmployeeToProject(Long projectId);
}
