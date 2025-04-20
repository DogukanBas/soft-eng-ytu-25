package com.softeng.backend.service;

import com.softeng.backend.model.Department;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {
    Department addDepartment(Department department);
    void setManager(Integer deptId, String personalNo);
    void setDepartmentRemainingBudget(Integer deptId, Double remainingBudget);
    void setDepartmentInitialBudget(Integer deptId, Double initialBudget);
    void resetDepartmentBudget(Integer deptId);
    List<Department> getAllDepartments();
    Department getDepartmentByName(String deptname);
    boolean existsByDeptname(String deptname);
    boolean existsByDeptId(Integer deptId);
}