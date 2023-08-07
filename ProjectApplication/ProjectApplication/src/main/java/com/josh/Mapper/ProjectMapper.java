package com.josh.Mapper;

import com.josh.DTO.ProjectDTO;
import com.josh.Entity.Project;

public class ProjectMapper {
    public ProjectMapper() {
    }
    public static Project projectMapper(ProjectDTO projectDTO){
        Project project=new Project();
        project.setProjectId(projectDTO.getProjectId());
        project.setProjectName(projectDTO.getProjectName());
        return project;
    }
    public static ProjectDTO projectDTOMapper(Project project){
        ProjectDTO projectDTO=new ProjectDTO();
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setProjectName(project.getProjectName());
        return projectDTO;
    }
}
