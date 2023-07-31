package com.josh.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Department")
public class Department {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    @Column(unique = true)
    private String departmentName;
}