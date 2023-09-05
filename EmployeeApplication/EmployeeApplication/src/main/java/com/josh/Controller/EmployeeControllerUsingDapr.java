package com.josh.Controller;

import com.josh.DTO.DepartmentDTO;
import com.josh.DTO.EmployeeDepartmentDTO;
import com.josh.DTO.EmployeeProjectDTO;
import com.josh.DTO.ProjectDTO;
import com.josh.Entity.Employee;
import com.josh.Entity.EmployeeProject;
import com.josh.Service.DaprExternalServiceClient;
import com.josh.Service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/emp")
@Slf4j
public class EmployeeControllerUsingDapr {

//    @Autowired
//    RestTemplate restTemplate;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    DaprExternalServiceClient daprExternalServiceClient;


    @PostMapping("/employee-department")
    public Mono<ResponseEntity<String>> addEmployeeDepartment(@RequestBody EmployeeDepartmentDTO employeeDepartmentDTO) throws Exception {
        Long departmentId = employeeDepartmentDTO.getDepartmentId();
        Long empId = employeeDepartmentDTO.getEmpId();

        System.out.println("Received department ID: " + departmentId);
        System.out.println("Received employee ID: " + empId);

        Mono<DepartmentDTO> departmentMono = daprExternalServiceClient.getDepartmentById(departmentId);

        return departmentMono.flatMap(departmentDTO -> {
            Employee employee = employeeService.getEmployee(empId);
            employee.setDepartmentId(departmentDTO.getDepartmentId());
            Employee updatedEmployee = employeeService.updateEmployee(employee, empId);

            String responseMessage = "Employee department added successfully.";
            return Mono.just(new ResponseEntity<>(responseMessage, HttpStatus.OK));
        }).defaultIfEmpty(new ResponseEntity<>("Department not found", HttpStatus.NOT_FOUND));
    }


    @PostMapping("/employee-project")
    public ResponseEntity<EmployeeProject> addEmployeeProject(@RequestBody EmployeeProjectDTO employeeProjectDTO) throws Exception {
        Long projectId = employeeProjectDTO.getProjectId();
        Long empId = employeeProjectDTO.getEmpId();

        System.out.println("Received project ID: " + projectId);
        System.out.println("Received employee ID: " + empId);

        Employee employee = employeeService.getEmployee(empId);
        Mono<ProjectDTO> projectDTOMono = daprExternalServiceClient.invokeGetProjectById(projectId);

        EmployeeProject employeeProject = new EmployeeProject();
        employeeProject.setProjectId(projectDTOMono.block().getProjectId());
        employeeProject.setEmpId(employee.getEmpId());


        return new ResponseEntity<EmployeeProject>(employeeService.addEmployeeProject(employeeProject), HttpStatus.OK);
    }

}

