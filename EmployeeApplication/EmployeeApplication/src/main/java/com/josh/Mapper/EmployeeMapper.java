package com.josh.Mapper;


import com.josh.DTO.EmployeeDTO;
import com.josh.DTO.EmployeeProjectDTO;
import com.josh.Entity.Employee;
import com.josh.Entity.EmployeeProject;

public class EmployeeMapper {

    private EmployeeMapper() {
    }

    public static Employee employeeMapper(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setEmpId(employeeDTO.getEmpId());
        employee.setDepartmentId(employeeDTO.getDepartmentId());
        //employee.setProjectId(employeeDTO.getProjectId());
        employee.setEmpName(employeeDTO.getEmpName());
        employee.setContactNumber(employeeDTO.getContactNumber());
        employee.setEmployeeAddress(employeeDTO.getEmployeeAddress());
        employee.setEmployeeBirthDate(employeeDTO.getEmployeeBirthDate());
        employee.setEmployeeJoiningDate(employeeDTO.getEmployeeJoiningDate());
        employee.setDepartmentId(employeeDTO.getDepartmentId());
        return employee;
    }

    public static EmployeeDTO employeeDTOMapper(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmpId(employee.getEmpId());
        employeeDTO.setDepartmentId(employee.getDepartmentId());
        employeeDTO.setEmpName(employee.getEmpName());
        employeeDTO.setContactNumber(employee.getContactNumber());
        employeeDTO.setEmployeeAddress(employee.getEmployeeAddress());
        employeeDTO.setEmployeeBirthDate(employee.getEmployeeBirthDate());
        employeeDTO.setEmployeeJoiningDate(employee.getEmployeeJoiningDate());
        employeeDTO.setDepartmentId(employee.getDepartmentId());
        return employeeDTO;
    }

    public static EmployeeProject employeeProjectMapper(EmployeeProjectDTO employeeProjectDTO) {
        EmployeeProject employeeProject = new EmployeeProject();
        employeeProject.setEmpId(employeeProjectDTO.getEmpId());
        employeeProject.setProjectId(employeeProjectDTO.getProjectId());

        return employeeProject;
    }

    public static EmployeeProjectDTO employeeProjectDTOMapper(EmployeeProject employeeProject) {
        EmployeeProjectDTO employeeProjectDTO = new EmployeeProjectDTO();
        employeeProjectDTO.setEmpId(employeeProject.getEmpId());
        employeeProjectDTO.setProjectId(employeeProject.getProjectId());

        return employeeProjectDTO;
    }


}