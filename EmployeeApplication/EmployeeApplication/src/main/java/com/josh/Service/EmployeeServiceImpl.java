package com.josh.Service;

import com.josh.Entity.Employee;
import com.josh.Entity.EmployeeProject;
import com.josh.Exception.ResourceNotFoundException;
import com.josh.Repository.EmployeeProjectRepository;
import com.josh.Repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

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
//    public Employee addEmployee(Employee employee) {
//        Employee saveEmployee = employeeRepository.save(employee);
//
//
//        String employeeMessage = "New Employee added with " + saveEmployee.getEmpId() + " and name " + saveEmployee.getEmpName();
//
//
//        notificationService.sendNotification(employeeMessage);
//        return saveEmployee;
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
//    public EmployeeProject addEmployeeProject(EmployeeProject employeeProject) {
//        Long empId = employeeProject.getEmpId();
//        Long projectId = employeeProject.getProjectId();
//
//        Optional<EmployeeProject> existingProject = employeeProjectRepository.findById(new EmployeeProjectId(empId, projectId));
//
//        if (existingProject.isPresent()) {
//            throw new ResourceNotFoundException("Employee project with empId " + empId + " and projectId " + projectId + " already exists.");
//        }
//
//        Employee employee = employeeRepository.findById(empId)
//                .orElseThrow(() -> new ResourceNotFoundException("Employee with ID " + empId + " not found."));
//
//        ProjectDTO projectDTO = externalServiceClient.getProjectById(projectId);
//
//        employeeProject.setEmpId(employee.getEmpId());
//        employeeProject.setProjectId(projectDTO.getProjectId());
//
//        return employeeProjectRepository.save(employeeProject);
//    }

    @Scheduled(fixedRate = 120000)
    public String lock() {
        var lock = lockRegistry.obtain(MY_LOCK_KEY);
        String returnVal = null;
        System.out.println("Scheduler Working: ");
        if (lock.tryLock()) {
            returnVal = "jdbc lock successful";
        } else {
            returnVal = "jdbc lock unsuccessful";
        }
        lock.unlock();
        return returnVal;
    }

    @Scheduled(fixedRate = 120000)
    @Override
    public String properLock() {
        Lock lock = null;

        try {
            //var  lock = lockRegistry.obtain(MY_LOCK_KEY);
            lock = lockRegistry.obtain(MY_LOCK_KEY);
        } catch (Exception e) {
            // in a production environment this should be a log statement
            System.out.println(String.format("Unable to obtain lock: %s", MY_LOCK_KEY));
        }

        String returnVal = null;
        try {
            if (lock.tryLock()) {
                returnVal = "jdbc lock successful!!!";
            } else {
                returnVal = "jdbc lock unsuccessful";
            }
        } catch (Exception e) {
            // in a production environment this should log and do something else
            e.printStackTrace();
        } finally {
            // always have this in a `finally` block in case anything goes wrong
            lock.unlock();
        }
        System.out.println("Scheduler Working: " + returnVal);
        return returnVal;
    }

    @Scheduled(fixedRate = 60000)
    public List<Employee> checkEmployeeWithoutProjectAllocation() throws InterruptedException {

        Lock lock = lockRegistry.obtain(MY_LOCK_KEY);

        boolean acquired = lock.tryLock(20, TimeUnit.SECONDS);
        if (acquired) {
            try {
                log.info("Acquired lock");

            } catch (Exception e) {
                e.printStackTrace();
            }

//            finally {
//                lock.unlock();
//                log.info("Release lock");
//            }

        }
        else {
            log.info("No lock");
        }


        return employeeRepository.employeeWithoutProjectAllocation();
        //return returnVal;

    }
}





