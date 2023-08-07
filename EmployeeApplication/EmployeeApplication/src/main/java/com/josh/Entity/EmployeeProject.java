package com.josh.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "employee_project")
@IdClass(EmployeeProjectId.class)
public class EmployeeProject implements Serializable  {
    @Id
    private Long empId;

    @Id
    private Long projectId;
}
