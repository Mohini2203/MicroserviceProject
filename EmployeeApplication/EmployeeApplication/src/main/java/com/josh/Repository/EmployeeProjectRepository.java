package com.josh.Repository;

import com.josh.Entity.EmployeeProject;
import com.josh.Entity.EmployeeProjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, EmployeeProjectId> {


}
