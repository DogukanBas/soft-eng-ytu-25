package com.softeng.backend.service;

import com.softeng.backend.model.Department;
import java.util.List;

public interface DepartmentService {
    Department addDepartment(Department department);
    void setManager(String deptName, String personalNo);
    void setDepartmentRemainingBudget(String deptName, Double remainingBudget);
    void setDepartmentInitialBudget(String deptName, Double initialBudget);
    void resetDepartmentBudget(String deptName);
    List<Department> getAllDepartments();
    List<String> getAllDepartmentNames();
    Department getDepartmentByName(String deptname);
    boolean existsByDeptname(String deptname);
    boolean existsByDeptId(Integer deptId);
}