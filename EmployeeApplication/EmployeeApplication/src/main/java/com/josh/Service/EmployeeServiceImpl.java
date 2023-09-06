package com.josh.Service;

import com.josh.Entity.Employee;
import com.josh.Entity.EmployeeProject;
import com.josh.Exception.ResourceNotFoundException;
import com.josh.Repository.EmployeeProjectRepository;
import com.josh.Repository.EmployeeRepository;
import io.lettuce.core.dynamic.support.ReflectionUtils;
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

            e.printStackTrace();
        } finally {

            lock.unlock();
        }
        System.out.println("Scheduler Working: " + returnVal);
        return returnVal;
    }

    @Scheduled(fixedRate = 60000)
    public List<Employee> checkEmployeeWithoutProjectAllocation() throws InterruptedException {

        Lock lock = lockRegistry.obtain(MY_LOCK_KEY);

        boolean acquired = lock.tryLock(1, TimeUnit.MINUTES);
        if (acquired) {
            try {

                log.info("Acquired Lock!");


                List<Employee> employees = employeeRepository.employeeWithoutProjectAllocation();

                for (Employee employee : employees) {
                    log.info("Checking employee: " + employee.getEmpId());


                    String notificationMessage = "The employee is not allocated to the project with id :" + employee.getEmpId();
                    notificationService.sendNotification(notificationMessage);
                }
            } catch (Exception e) {
                ReflectionUtils.rethrowRuntimeException(e);
            }
//            finally {
//                lock.unlock();
//                log.info("Released Lock!");
//            }
        } else {
            log.info("Another instance is already running.");
        }

        return null;
    }
}




