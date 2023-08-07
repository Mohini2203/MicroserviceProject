package com.josh.Controller;

import com.josh.DTO.EmployeeProjectDTO;
import com.josh.DTO.ProjectDTO;
import com.josh.Entity.Project;
import com.josh.Mapper.ProjectMapper;
import com.josh.Service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Value("${appId}")
    private String ProjectAppId;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<Project> projects = projectService.getAllProject();
         List<ProjectDTO> projectDTO = projects.stream().map(ProjectMapper::projectDTOMapper).collect(Collectors.toList());
       // List<ProjectDTO>projectDTO=projects.stream().map(ProjectMapper::projectDTOMapper).
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable("id") Long id) {
        Project project = projectService.getProject(id);
        ProjectDTO projectDTO = ProjectMapper.projectDTOMapper(project);
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> addProject(@RequestBody ProjectDTO projectDTO) {
        Project projectToAdd = ProjectMapper.projectMapper(projectDTO);
        Project addedProject = projectService.addProject(projectToAdd);
        ProjectDTO addedProjectDTO = ProjectMapper.projectDTOMapper(addedProject);
        return new ResponseEntity<>(addedProjectDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateDepartment(@RequestBody ProjectDTO projectDTO, @PathVariable("id") Long id) {
        Project projectToUpdate = ProjectMapper.projectMapper(projectDTO);
        Project updatedProject = projectService.updateProject(projectToUpdate, id);
        ProjectDTO updatedProjectDTO = ProjectMapper.projectDTOMapper(updatedProject);
        return new ResponseEntity<>(updatedProjectDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable("projectId") Long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @PostMapping("/projects/addEmployee")
//    public ResponseEntity<String> addEmployeeProject(@RequestBody EmployeeProjectDTO employeeProjectDTO) {
//        Long projectId = employeeProjectDTO.getProjectId();
//        boolean isAddedToProject = projectService.addEmployeeToProject(projectId);
//
//        if (isAddedToProject) {
//            return ResponseEntity.ok("Employee added to the project successfully.");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
//        }
//    }
}
