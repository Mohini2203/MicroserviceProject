package com.josh.Service;

import com.josh.DTO.DepartmentDTO;
import com.josh.DTO.ProjectDTO;
import com.josh.Exception.EmployeeServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@Service
public class ExternalServiceClient {
    @Autowired
    RestTemplate restTemplate;

//    @Value("${external.service.department.url}")
//    private String departmentServiceUrl;

//    @Value("${external.service.project.url}")
//    private String projectServiceUrl;

//    @Value("${external.service.department.appId}")
//    private String departmentAppId;
//
//    @Value("${external.service.project.appId}")
//    private String ProjectAppId;

    @Autowired
    public ExternalServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DepartmentDTO getDepartmentById(Long departmentId) {
        try {
            ResponseEntity<DepartmentDTO> response = restTemplate.exchange(
                    "http://localhost:8086/departments/{departmentId}",
                   // departmentServiceUrl+departmentId,
                    HttpMethod.GET,
                    null,
                    DepartmentDTO.class,
                    departmentId
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new EmployeeServiceException("Department with ID " + departmentId + " not found.");
            } else {
                throw new EmployeeServiceException("Error connecting to the department service. Status code: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new EmployeeServiceException("Department with ID " + departmentId + " not found.");
            } else {
                throw new EmployeeServiceException("Error connecting to the department service. Status code: " + ex.getStatusCode());
            }
        } catch (ResourceAccessException ex) {
            throw new EmployeeServiceException("Error connecting to the department service: " + ex.getMessage());
        }
    }

    public ProjectDTO getProjectById(Long projectId) {
        try {
            ResponseEntity<ProjectDTO> response = restTemplate.exchange(
                  "http://localhost:8087/projects/{projectId}",
                    //projectServiceUrl+projectId,
                    HttpMethod.GET,
                    null,
                    ProjectDTO.class,
                    projectId
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new EmployeeServiceException("Project with ID " + projectId + " not found.");
            } else {
                throw new EmployeeServiceException("Error connecting to the project service. Status code: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new EmployeeServiceException("Project with ID " + projectId + " not found.");
            } else {
                throw new EmployeeServiceException("Error connecting to the project service. Status code: " + ex.getStatusCode());
            }
        } catch (ResourceAccessException ex) {
            throw new EmployeeServiceException("Error connecting to the project service: " + ex.getMessage());
        }
    }


}