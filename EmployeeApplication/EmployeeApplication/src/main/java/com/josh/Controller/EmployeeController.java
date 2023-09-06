package com.josh.Controller;

import com.josh.DTO.*;
import com.josh.Entity.Employee;
import com.josh.Entity.EmployeeProject;
import com.josh.Mapper.EmployeeMapper;
import com.josh.Service.EmployeeService;
import com.josh.Service.ExternalServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    ExternalServiceClient externalServiceClient;

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployee() {
        List<Employee> employees = employeeService.getAllEmployee();

        List<EmployeeDTO> employeeDTOList = employees.stream().map(EmployeeMapper::employeeDTOMapper).collect(Collectors.toList());
        return new ResponseEntity<>(employeeDTOList, HttpStatus.OK);
    }

    @GetMapping("/{empId}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable("empId") Long empId) {
        Employee employee = employeeService.getEmployee(empId);
        EmployeeDTO employeeDTO = EmployeeMapper.employeeDTOMapper(employee);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employeeToAdd = EmployeeMapper.employeeMapper(employeeDTO);
        Employee employee = employeeService.addEmployee(employeeToAdd);
        EmployeeDTO employeeDTOAdded = EmployeeMapper.employeeDTOMapper(employee);
        return new ResponseEntity<>(employeeDTOAdded, HttpStatus.OK);
    }

    //updating employee by id
    @PutMapping("/{empId}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable("empId") Long empId) {
        Employee employeeToUpdate = EmployeeMapper.employeeMapper(employeeDTO);
        Employee employee = employeeService.updateEmployee(employeeToUpdate, empId);
        EmployeeDTO updatedEmployeeDTO = EmployeeMapper.employeeDTOMapper(employee);
        return new ResponseEntity<>(updatedEmployeeDTO, HttpStatus.OK);
    }


    @DeleteMapping("/{empId}")
    public ResponseEntity<HttpStatus> deleteAllEmployees() {
        employeeService.deleteAllEmployees();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/add-employee-department")
    public ResponseEntity<String> addEmployeeDepartment(@RequestBody EmployeeDepartmentDTO employeeDepartmentDTO) {
        DepartmentDTO department = externalServiceClient.getDepartmentById(employeeDepartmentDTO.getDepartmentId());
        Employee employee = employeeService.getEmployee(employeeDepartmentDTO.getEmpId());
        employee.setDepartmentId(department.getDepartmentId());
        Employee updatedEmployee = employeeService.updateEmployee(employee, employeeDepartmentDTO.getEmpId());

        String responseMessage = "Employee department added successfully.";
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }


    @PostMapping("/add_employee_project")
    public ResponseEntity<String> addEmployeeProject(@RequestBody EmployeeProjectDTO employeeProjectDTO) {
        ProjectDTO projectDTO = externalServiceClient.getProjectById(employeeProjectDTO.getProjectId());
        Employee employee = employeeService.getEmployee(employeeProjectDTO.getEmpId());

        EmployeeProject employeeProject = new EmployeeProject();
        employeeProject.setEmpId(employee.getEmpId());
        employeeProject.setProjectId(projectDTO.getProjectId());

        employeeService.addEmployeeProject(employeeProject);

        String responseMessage = "Employee project added successfully.";
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PutMapping("/lock")
    public String lock(){
        return employeeService.lock();
    }
    @PutMapping("/properLock")
    public String properLock(){
        return employeeService.properLock();
    }

    @GetMapping("/employee-without-project")
    public ResponseEntity<List<Employee>> employeeWithoutProject() throws InterruptedException {
        List<Employee>employees=employeeService.checkEmployeeWithoutProjectAllocation();
        return ResponseEntity.ok(employees);

    }
}
