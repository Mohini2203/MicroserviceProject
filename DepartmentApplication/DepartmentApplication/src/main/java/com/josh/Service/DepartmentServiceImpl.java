package com.josh.Service;

import com.josh.Entity.Department;
import com.josh.Exception.ResourceNotFoundException;
import com.josh.Repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department getDepartmentByName(String name) {
        return departmentRepository.findByDepartmentName(name);
    }

    @Override
    public List<Department> getAllDepartment() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartment(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Department Exist With Id :" + id));
    }

    @Override
    public Department addDepartment(Department department) {
        Department existDepartment = getDepartmentByName(department.getDepartmentName());
        if (existDepartment == null) {
            return departmentRepository.save(department);

        } else {
            throw new ResourceNotFoundException("Department Already Exist  ID " + existDepartment.getDepartmentId());
        }
    }

    @Override
    public Department updateDepartment(Department department, Long id) {
        Department departmentToUpdate = getDepartment(id);
        departmentToUpdate.setDepartmentName(department.getDepartmentName());
        return departmentRepository.save(departmentToUpdate);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = getDepartment(id);
        if (department != null) {
            departmentRepository.deleteById(id);
        }
    }
}