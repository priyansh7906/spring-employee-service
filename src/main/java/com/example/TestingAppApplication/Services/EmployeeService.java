package com.example.TestingAppApplication.Services;

import com.example.TestingAppApplication.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto getEmployeeById(Long id);

    EmployeeDto createNewEmployee(EmployeeDto employeeDto);

    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);

    void deleteEmployeeById(Long id);

    List<EmployeeDto> getAllEmployees();
}
