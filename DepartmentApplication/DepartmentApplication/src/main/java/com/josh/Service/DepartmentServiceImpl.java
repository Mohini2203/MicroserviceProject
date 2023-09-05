package com.josh.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.josh.Entity.Department;
import com.josh.Exception.ResourceNotFoundException;
import com.josh.Repository.DepartmentRepository;
import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.RequiredTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {
//    @Autowired
//    private DepartmentRepository departmentRepository;

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

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

  //  private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);

//    public void processCloudEvent(CloudEvent<String> cloudEvent) {
//        try {
//
//            log.info("Processing cloud event data: " + cloudEvent.getData());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
