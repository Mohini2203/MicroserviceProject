package com.josh.Service;

import com.josh.Entity.Employee;
import com.josh.Entity.EmployeeProject;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployee();

    Employee getEmployee(Long id);

    Employee addEmployee(Employee e);

    Employee updateEmployee(Employee e, Long id);

    void deleteAllEmployees();

    EmployeeProject addEmployeeProject(EmployeeProject employeeProject);
}
