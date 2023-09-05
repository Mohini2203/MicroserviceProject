package com.josh.Service;


import com.josh.Entity.Department;
import io.dapr.client.domain.CloudEvent;

import java.util.List;

public interface DepartmentService {

    Department getDepartmentByName(String name);

    List<Department> getAllDepartment();

    Department getDepartment(Long id);

    Department addDepartment(Department department);


    Department updateDepartment(Department department, Long id);

    void deleteDepartment(Long id);

   // void processCloudEvent(CloudEvent<String> cloudEvent);
}