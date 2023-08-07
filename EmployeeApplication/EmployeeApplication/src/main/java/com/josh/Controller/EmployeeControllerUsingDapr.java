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
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/emp")
@Slf4j
public class EmployeeControllerUsingDapr {

    @Autowired
    RestTemplate restTemplate;

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




    @PostMapping("/employee_project")
    public Mono<ResponseEntity<String>> addEmployeeProject(@RequestBody EmployeeProjectDTO employeeProjectDTO) throws Exception {
     log.info("project id" +employeeProjectDTO.getProjectId());
     log.info("employee id" +employeeProjectDTO.getEmpId());
        return daprExternalServiceClient.getProjectById(employeeProjectDTO.getProjectId())
                .flatMap(projectDTO -> {
                    Mono<Employee> employeeMono = Mono.fromCallable(() ->
                            employeeService.getEmployee(employeeProjectDTO.getEmpId()));

                    return employeeMono.flatMap(employee -> {
                        EmployeeProject employeeProject = new EmployeeProject();
                        employeeProject.setEmpId(employee.getEmpId());
                        employeeProject.setProjectId(projectDTO.getProjectId());

                        return Mono.fromRunnable(() -> employeeService.addEmployeeProject(employeeProject))
                                .thenReturn(new ResponseEntity<>("Employee project added successfully.", HttpStatus.OK));
                    });
                })
                .onErrorResume(Exception.class, ex ->
                        Mono.just(new ResponseEntity<>("Error adding employee project.", HttpStatus.INTERNAL_SERVER_ERROR))
                );
    }
}
