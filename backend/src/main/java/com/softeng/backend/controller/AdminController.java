package com.softeng.backend.controller;

import com.softeng.backend.dto.AdminDTOs;
import com.softeng.backend.model.Employee;
import com.softeng.backend.model.User;
import com.softeng.backend.service.DepartmentService;
import com.softeng.backend.service.EmployeeService;
import com.softeng.backend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    @Autowired
    public AdminController(UserService userService, EmployeeService employeeService, DepartmentService departmentService) {
        this.userService = userService;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @Transactional
    @PostMapping("/add-employee")
    public ResponseEntity<?> addEmployee(@RequestBody AdminDTOs.AddEmployeeRequest request, Authentication authentication) {
        logger.debug("Adding user with personalNo: {}", request.getPersonalNo());
        String personaNo = authentication.getName();
        User currentUser = userService.getUserByPersonalNo(personaNo);
        logger.debug("Current User1903: {}", personaNo);

        if (currentUser.getUserType() != User.UserType.admin) {
            return ResponseEntity.status(403).body(AdminDTOs.AddEmployeeResponse.INVALID_AUTHENTICATION.getMessage());
        }

        String personalNo = request.getPersonalNo();
        String email = request.getEmail();

        if (userService.existsByPersonalNo(personalNo) || userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(AdminDTOs.AddEmployeeResponse.EMPLOYEE_ALREADY_EXISTS.getMessage());
        }

        try {
            User user = new User(request.getPersonalNo(),
                    request.getEmail(),
                    request.getUserType());
            userService.addUser(user, request.getPassword());

            User managedUser = userService.getUserByPersonalNo(user.getPersonalNo());
            Employee employee = new Employee(managedUser,
                    request.getName(),
                    request.getSurname(),
                    departmentService.getDepartmentByName(request.getDeptName()));
            employeeService.addEmployee(employee);

            if (user.getUserType() == User.UserType.manager) {
                departmentService.setManager(employee.getDepartment().getDeptId(),
                        employee.getPersonalNo());
            }

            logger.info("Employee added successfully: {}", employee);

        }
        catch (Exception e) {
            logger.error("Error while adding employee: {}", e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseEntity.status(500).body("Internal server error");
        }

        return ResponseEntity.ok(AdminDTOs.AddEmployeeResponse.EMPLOYEE_ADDED.getMessage());
    }
}
