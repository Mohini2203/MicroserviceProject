package com.josh.Service;

import com.josh.DTO.ProjectDTO;
import com.josh.Entity.Employee;
import com.josh.Entity.EmployeeProject;

import com.josh.Entity.EmployeeProjectId;
import com.josh.Exception.ResourceNotFoundException;
import com.josh.Repository.EmployeeProjectRepository;
import com.josh.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    EmployeeProjectRepository employeeProjectRepository;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ExternalServiceClient externalServiceClient;

    @Autowired
    DaprExternalServiceClient daprExternalServiceClient;

    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployee(Long empId) {
        return employeeRepository.findById(empId).orElseThrow(() -> new ResourceNotFoundException("No Employee Exist With Id : " + empId));
    }

    @Override
    public Employee addEmployee(Employee employee) {

        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee, Long empId) {
        Employee existedEmployee = getEmployee(empId);
        if (existedEmployee != null) {
            return employeeRepository.save(employee);
        }
        return null;
    }

    @Override
    public void deleteAllEmployees() {
        employeeRepository.deleteAll();
    }

//        @Override
//    public EmployeeProject addEmployeeProject(EmployeeProject employeeProject) {
//        return employeeProjectRepository.save(employeeProject);
//    }
//    @Override
//    public EmployeeProject addEmployeeProject(EmployeeProject employeeProject) {
//        Long empId = employeeProject.getEmpId();
//        Long projectId = employeeProject.getProjectId();
//
//        // Check if the resource with the given composite key (empId and projectId) already exists in the database
//        Optional<EmployeeProject> existingProject = employeeProjectRepository.findById(new EmployeeProjectId(empId, projectId));
//
//        if (existingProject.isPresent()) {
//            throw new ResourceNotFoundException("Employee project with empId " + empId + " and projectId " + projectId + " already exists.");
//        }
//
//        // Check if the employee with the given empId exists in the database
//        Employee employee = employeeRepository.findById(empId)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee with ID " + empId + " not found."));
//
//        // If both the employee and project exist, save the new employee project
//        return employeeProjectRepository.save(employeeProject);
//    }

    @Override
    public EmployeeProject addEmployeeProject(EmployeeProject employeeProject) {
        Long empId = employeeProject.getEmpId();
        Long projectId = employeeProject.getProjectId();

        Optional<EmployeeProject> existingProject = employeeProjectRepository.findById(new EmployeeProjectId(empId, projectId));

        if (existingProject.isPresent()) {
            throw new ResourceNotFoundException("Employee project with empId " + empId + " and projectId " + projectId + " already exists.");
        }

        Employee employee = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with ID " + empId + " not found."));

        ProjectDTO projectDTO = externalServiceClient.getProjectById(projectId);

        employeeProject.setEmpId(employee.getEmpId());
        employeeProject.setProjectId(projectDTO.getProjectId());

        return employeeProjectRepository.save(employeeProject);
    }
}




