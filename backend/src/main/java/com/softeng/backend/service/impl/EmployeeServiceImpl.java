package com.softeng.backend.service.impl;

import com.softeng.backend.exception.ResourceNotFoundException;
import com.softeng.backend.model.Employee;
import com.softeng.backend.repository.EmployeeRepository;
import com.softeng.backend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public void addEmployee(Employee employee) {
        try {
            System.out.println("Adding employee: " + employee.toString());
            employeeRepository.save(employee);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error adding employee: " + e.getMessage());
        }
    }

    @Override
    public Employee getEmployeeByPersonalNo(String personalNo) {
        return employeeRepository.findByPersonalNo(personalNo)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with personal number: " + personalNo));
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
