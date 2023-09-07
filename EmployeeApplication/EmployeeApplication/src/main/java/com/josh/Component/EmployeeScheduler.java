package com.josh.Component;

import com.josh.Entity.Employee;
import com.josh.Repository.EmployeeRepository;
import com.josh.Service.NotificationService;
import io.lettuce.core.dynamic.support.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Component
@Slf4j
public class EmployeeScheduler {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    NotificationService notificationService;
    private static final String MY_LOCK_KEY = "someLockKey";
    private final LockRegistry lockRegistry;

    public EmployeeScheduler(LockRegistry lockRegistry) {
        this.lockRegistry = lockRegistry;
    }

    @Scheduled(fixedRate = 60000)
    public List<Employee> checkEmployeeWithoutProjectAllocation() throws InterruptedException {

        Lock lock = lockRegistry.obtain(MY_LOCK_KEY);

        boolean acquired = lock.tryLock(1, TimeUnit.MINUTES);
        if (acquired) {
            try {

                log.info("Acquired Lock!");


                List<Employee> employees = employeeRepository.getEmployeeWithoutProjectAllocation();

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


