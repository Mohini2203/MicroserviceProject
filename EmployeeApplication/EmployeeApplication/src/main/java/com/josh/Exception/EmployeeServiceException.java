package com.josh.Exception;

public class EmployeeServiceException extends RuntimeException{

    public EmployeeServiceException(String message) {
        super(message);
    }
}
