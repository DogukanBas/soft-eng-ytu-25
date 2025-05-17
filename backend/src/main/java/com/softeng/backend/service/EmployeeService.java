package com.softeng.backend.service;
import com.softeng.backend.model.Employee;

import java.util.List;

public interface EmployeeService {
    void addEmployee(Employee employee);
    Employee getEmployeeByPersonalNo(String personalNo);
    List<Employee> getAllEmployees();
    boolean existsByPersonalNo(String personalNo);
}
