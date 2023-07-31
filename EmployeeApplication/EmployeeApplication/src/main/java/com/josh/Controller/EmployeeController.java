package com.josh.Controller;

import com.josh.DTO.*;
import com.josh.Entity.Employee;
import com.josh.Entity.EmployeeProject;
import com.josh.Mapper.EmployeeMapper;
import com.josh.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployee() {
        List<Employee> employees = employeeService.getAllEmployee();

        // List<EmployeeDTO> employeeDTOList = employees.stream().map(EmployeeMapper::employeeDTOMapper).toList();


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
    public ResponseEntity<EmployeeDTO> addEmployeeDepartment(@RequestBody EmployeeDepartmentDTO employeeDepartmentDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<DepartmentDTO> entity = new HttpEntity<DepartmentDTO>(headers);
        String httpRequest = "http://localhost:8082/departments/" + employeeDepartmentDTO.getDepartmentId();
        DepartmentDTO department = restTemplate.exchange(httpRequest, HttpMethod.GET, entity, DepartmentDTO.class).getBody();

        Employee employee = employeeService.getEmployee(employeeDepartmentDTO.getEmpId());
        employee.setDepartmentId(department.getDepartmentId());
        Employee updatedEmployee = employeeService.updateEmployee(employee, employeeDepartmentDTO.getEmpId());
        //return new ResponseEntity<EmployeeDTO>((MultiValueMap<String, String>) updatedEmployee, HttpStatus.OK);
        return null;
    }


    @PostMapping("/add_employee_project")
    public ResponseEntity<EmployeeDTO> addEmployeeProject(@RequestBody EmployeeProjectDTO employeeProjectDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<ProjectDTO> entity = new HttpEntity<>(headers);
        String httpRequest = "http://localhost:8083/projects/" + employeeProjectDTO.getProjectId();
        ProjectDTO projectDTO = restTemplate.exchange(httpRequest, HttpMethod.GET, entity, ProjectDTO.class).getBody();
        Employee employee = employeeService.getEmployee(employeeProjectDTO.getEmpId());

        EmployeeProject employeeProject = new EmployeeProject();
        employeeProject.setEmpId(employee.getEmpId());
        employeeProject.setProjectId(projectDTO.getProjectId());

        employeeService.addEmployeeProject(employeeProject);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
