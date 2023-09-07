package com.josh.Repository;

import com.josh.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    //  Employee save(Employee employee);


    @Query("SELECT e FROM Employee e WHERE e.empId NOT IN (SELECT ep.empId FROM EmployeeProject ep)")
   // @Query(value = "SELECT e.empId, e.empName FROM Employee e LEFT JOIN EmployeeProject ep ON e.empId = ep.empId WHERE ep.empId IS NULL", nativeQuery = true)
    List<Employee> getEmployeeWithoutProjectAllocation();


}
