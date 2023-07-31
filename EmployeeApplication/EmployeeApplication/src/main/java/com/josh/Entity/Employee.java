package com.josh.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long empId;


    private String empName;


    private String contactNumber;


    private String employeeAddress;


    private LocalDate employeeBirthDate;


    private LocalDate employeeJoiningDate;

    @JsonIgnore
    private Long departmentId;

//    private Long projectId;





}