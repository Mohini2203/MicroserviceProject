package com.josh.Service;

import com.josh.Entity.Employee;
import com.josh.Entity.EmployeeProject;
import com.josh.Exception.ResourceNotFoundException;
import com.josh.Repository.EmployeeProjectRepository;
import com.josh.Repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
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
    NotificationService notificationService;
    @Autowired
    DaprExternalServiceClient daprExternalServiceClient;


    private static final String MY_LOCK_KEY = "someLockKey";
    private final LockRegistry lockRegistry;


    public EmployeeServiceImpl(LockRegistry lockRegistry) {
        this.lockRegistry = lockRegistry;
    }

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

        Employee saveEmployee = employeeRepository.save(employee);
        String notificationMessage = "New Employee added with ID: " + saveEmployee.getEmpId() + " and name: " + saveEmployee.getEmpName();

        notificationService.sendNotification(notificationMessage);
        return saveEmployee;
    }
//    @Override
//    public List<Employee> addEmployee(List<Employee> employees) {
//        List<Employee> savedEmployees = employeeRepository.saveAll(employees);
//
//        for (Employee savedEmployee : savedEmployees) {
//            String notificationMessage = "New Employee added with ID: " + savedEmployee.getEmpId() + " and name: " + savedEmployee.getEmpName();
//            notificationService.sendNotification(Collections.singletonList(notificationMessage));
//        }
//
//        return savedEmployees;
//    }


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

    @Override
    public EmployeeProject addEmployeeProject(EmployeeProject employeeProject) {
        return employeeProjectRepository.save(employeeProject);
    }

//    @Override
//    public List<Employee> getEmployeeWithoutProjectAllocation() {
//        return employeeRepository.employeeWithoutProjectAllocation();
//    }


//    @Scheduled(fixedRate = 120000)
//    public String lock() {
//        var lock = lockRegistry.obtain(MY_LOCK_KEY);
//        String returnVal = null;
//        System.out.println("Scheduler Working: ");
//        if (lock.tryLock()) {
//            returnVal = "jdbc lock successful";
//        } else {
//            returnVal = "jdbc lock unsuccessful";
//        }
//        lock.unlock();
//        return returnVal;
//    }
//
//    @Scheduled(fixedRate = 120000)
//    @Override
//    public String properLock() {
//        Lock lock = null;
//
//        try {
//            //var  lock = lockRegistry.obtain(MY_LOCK_KEY);
//            lock = lockRegistry.obtain(MY_LOCK_KEY);
//        } catch (Exception e) {
//
//            System.out.println(String.format("Unable to obtain lock: %s", MY_LOCK_KEY));
//        }
//
//        String returnVal = null;
//        try {
//            if (lock.tryLock()) {
//                returnVal = "jdbc lock successful!!!";
//            } else {
//                returnVal = "jdbc lock unsuccessful";
//            }
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        } finally {
//
//            lock.unlock();
//        }
//        System.out.println("Scheduler Working: " + returnVal);
//        return returnVal;
//    }


}




