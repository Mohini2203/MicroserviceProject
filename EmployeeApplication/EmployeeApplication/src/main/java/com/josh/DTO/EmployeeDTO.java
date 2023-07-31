package com.josh.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    private Long empId;

    @NotBlank(message = "Employee name is required!!!")
    private String empName;

    @Pattern(regexp = "^(|\\d{10})$", message = "Contact number should be empty or 10 digits")
    private String contactNumber;

    @NotBlank(message = "Employee address is required!!")
    private String employeeAddress;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @Past(message = "Employee DOB should be a past date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate employeeBirthDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    @PastOrPresent(message = "Employee joining date should be a past or present date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate employeeJoiningDate;

    private Long departmentId;

  //  private Long projectId;

}