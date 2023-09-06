package com.josh.Service;

import com.josh.Entity.Project;
import com.josh.Exception.ResourceNotFoundException;
import com.josh.Repository.ProjectRepository;
import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements  ProjectService {

    @Autowired
    ProjectRepository projectRepository;



    @Override
    public Project getProjectByName(String projectName) {
        return projectRepository.findByProjectName(projectName);
    }

    @Override
    public List<Project> getAllProject() {
        return projectRepository.findAll();
    }


    @Override
    public Project getProject(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Project Exist With Id :" + id));

    }

    @Override
    public Project addProject(Project project) {
        Project existProject = getProjectByName(project.getProjectName());
        if (existProject == null) {
            return projectRepository.save(project);
        } else {
            throw new IllegalArgumentException("Project with name '" + project.getProjectName() + "' already exists.");
        }
    }

    @Override
    public Project updateProject(Project project, Long projectId) {
        Project projectToUpdate = getProject(projectId);
        projectToUpdate.setProjectName(project.getProjectName());
        return projectRepository.save(projectToUpdate);
    }

    @Override
    public void deleteProject(Long projectId) {
        Project project = getProject(projectId);
        if (project != null) {
            projectRepository.deleteById(projectId);
        }
    }

//    @Topic(name = "employee-topic", pubsubName = "kafka-pubsub")
//    @KafkaListener(topics = "employee-topic")
//    public void consumeEmployeeMessage(CloudEvent event) {
//       // String employeeMessage = event.getData().toObject(String.class);
//        String employeeMessage = (String) event.getData();
//       //log.info (String.format("employee event received in stock service => %s", event.toString()));
//
//        System.out.println("Received Employee Message in Project Service: " + employeeMessage);
//
//    }

//    public boolean addEmployeeToProject(Long projectId) {
//        Project project = projectRepository.findById(projectId).orElse(null);
//
//        if (project == null) {
//            return false;
//        }
//
//        // Update both sides of the many-to-many relationship
//        // Employee employeeToAdd = ... // Fetch the employee using some method
//        // project.getEmployees().add(employeeToAdd);
//
//        projectRepository.save(project);
//
//        return true;
//    }

}
