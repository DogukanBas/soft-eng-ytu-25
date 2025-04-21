package com.softeng.backend.service;

import com.softeng.backend.model.Department;
import java.util.List;

public interface DepartmentService {
    Department addDepartment(Department department);
    void setManager(Integer deptId, String personalNo);
    void setDepartmentRemainingBudget(Integer deptId, Double remainingBudget);
    void setDepartmentInitialBudget(Integer deptId, Double initialBudget);
    void resetDepartmentBudget(Integer deptId);
    List<Department> getAllDepartments();
    List<String> getAllDepartmentNames();
    Department getDepartmentByName(String deptname);
    boolean existsByDeptname(String deptname);
    boolean existsByDeptId(Integer deptId);
}