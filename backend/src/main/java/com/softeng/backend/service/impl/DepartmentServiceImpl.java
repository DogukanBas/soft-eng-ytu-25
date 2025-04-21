package com.softeng.backend.service.impl;

import com.softeng.backend.model.Department;
import com.softeng.backend.repository.DepartmentRepository;
import com.softeng.backend.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
    @Override
    public Department addDepartment(Department department) {
try {
            return departmentRepository.save(department);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error adding department: " + e.getMessage());
        }
    }

    @Override
    public void setManager(Integer deptId, String personalNo) {
        departmentRepository.setManager(deptId, personalNo);
    }

    @Override
    public void setDepartmentRemainingBudget(Integer deptId, Double remainingBudget) {
        departmentRepository.setDepartmentRemainingBudget(deptId, remainingBudget);
    }

    @Override
    public void setDepartmentInitialBudget(Integer deptId, Double initialBudget) {
        departmentRepository.setDepartmentInitialBudget(deptId, initialBudget);
    }

    @Override
    public void resetDepartmentBudget(Integer deptId) {
        departmentRepository.resetDepartmentBudget(deptId);
    }

    @Override
    public List<String> getAllDepartmentNames() {
        return departmentRepository.getAllDepartmentNames();
    }
    @Override
    public Department getDepartmentByName(String deptname) {
        return departmentRepository.findByDeptname(deptname)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with name: " + deptname));
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public boolean existsByDeptname(String deptname) {
        return departmentRepository.existsByDeptname(deptname);
    }

    @Override
    public boolean existsByDeptId(Integer deptId) {
        return departmentRepository.existsByDeptId(deptId);
    }
}
