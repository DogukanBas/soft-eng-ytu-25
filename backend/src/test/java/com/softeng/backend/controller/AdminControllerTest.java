package com.softeng.backend.controller;

import com.softeng.backend.dto.AdminDTOs;
import com.softeng.backend.model.Department;
import com.softeng.backend.model.User;
import com.softeng.backend.service.DepartmentService;
import com.softeng.backend.service.EmployeeService;
import com.softeng.backend.service.NotificationService;
import com.softeng.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private UserService userService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        when(authentication.getName()).thenReturn("adminUser");

        User adminUser = new User("adminUser", "admin@example.com", User.UserType.admin);
        when(userService.getUserByPersonalNo("adminUser")).thenReturn(adminUser);
    }

    @Test
    @Transactional
    void addEmployee_success() {
        AdminDTOs.AddEmployeeRequest request = new AdminDTOs.AddEmployeeRequest();
        request.setPersonalNo("12345");
        request.setEmail("test@example.com");
        request.setUserType(User.UserType.team_member);
        request.setDeptName("IT");
        request.setPassword("password");
        request.setName("Test");
        request.setSurname("User");

        when(userService.existsByPersonalNo("12345")).thenReturn(false);
        when(userService.existsByEmail("test@example.com")).thenReturn(false);
        Department dept = new Department("IT");
        dept.setDeptId(1);
        when(departmentService.getDepartmentByName("IT")).thenReturn(dept);

        ResponseEntity<?> response = adminController.addEmployee(request, authentication);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Map.of("message", AdminDTOs.AddEmployeeResponse.EMPLOYEE_ADDED.getMessage()), response.getBody());
    }

    @Test
    void addEmployee_alreadyExists() {
        AdminDTOs.AddEmployeeRequest request = new AdminDTOs.AddEmployeeRequest();
        request.setPersonalNo("12345");
        request.setEmail("test@example.com");

        when(userService.existsByPersonalNo("12345")).thenReturn(true);

        ResponseEntity<?> response = adminController.addEmployee(request, authentication);

        assertEquals(409, response.getStatusCodeValue());
        assertEquals(AdminDTOs.AddEmployeeResponse.EMPLOYEE_ALREADY_EXISTS.getMessage(), response.getHeaders().getFirst("message"));
    }

    @Test
    @Transactional
    void addDepartment_success() {
        AdminDTOs.AddDepartmentRequest request = new AdminDTOs.AddDepartmentRequest();
        request.setDeptName("HR");

        when(departmentService.existsByDeptname("HR")).thenReturn(false);

        ResponseEntity<?> response = adminController.addDepartment(request, authentication);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Map.of("message", AdminDTOs.AddDepartmentResponse.DEPARTMENT_ADDED.getMessage()), response.getBody());
    }

    @Test
    void addDepartment_alreadyExists() {
        AdminDTOs.AddDepartmentRequest request = new AdminDTOs.AddDepartmentRequest();
        request.setDeptName("HR");

        when(departmentService.existsByDeptname("HR")).thenReturn(true);

        ResponseEntity<?> response = adminController.addDepartment(request, authentication);

        assertEquals(409, response.getStatusCodeValue());
        assertEquals(AdminDTOs.AddDepartmentResponse.DEPARTMENT_ALREADY_EXISTS.getMessage(), response.getHeaders().getFirst("message"));
    }

    @Test
    void getAllDepartmentNames_success() {
        when(departmentService.getAllDepartmentNames()).thenReturn(List.of("IT", "HR"));

        ResponseEntity<?> response = adminController.getAllDepartmentNames(authentication);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Map.of("departments", List.of("IT", "HR")), response.getBody());
    }

    @Test
    void addEmployee_nonAdminUser() {
        AdminDTOs.AddEmployeeRequest request = new AdminDTOs.AddEmployeeRequest();
        request.setPersonalNo("12345");
        request.setEmail("test@example.com");
        request.setUserType(User.UserType.team_member);
        request.setDeptName("IT");
        request.setPassword("password");
        request.setName("Test");
        request.setSurname("User");

        User nonAdminUser = new User("nonAdminUser", "user@example.com", User.UserType.team_member);
        when(userService.getUserByPersonalNo("adminUser")).thenReturn(nonAdminUser);

        ResponseEntity<?> response = adminController.addEmployee(request, authentication);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals(AdminDTOs.AddEmployeeResponse.INVALID_AUTHENTICATION.getMessage(), response.getHeaders().getFirst("message"));
    }

    @Test
    void addManager_departmentAlreadyHasManager() {
        AdminDTOs.AddEmployeeRequest request = new AdminDTOs.AddEmployeeRequest();
        request.setPersonalNo("12345");
        request.setEmail("manager@example.com");
        request.setUserType(User.UserType.manager);
        request.setDeptName("IT");
        request.setPassword("password");
        request.setName("Manager");
        request.setSurname("User");

        Department dept = new Department("IT");
        dept.setDeptId(1);
        dept.setDeptManager(new User("123","existing@example.com", User.UserType.manager));
        when(departmentService.existsByDeptname("IT")).thenReturn(true);
        when(departmentService.getDepartmentByName("IT")).thenReturn(dept);

        ResponseEntity<?> response = adminController.addEmployee(request, authentication);

        assertEquals(409, response.getStatusCodeValue());
        assertEquals(AdminDTOs.AddEmployeeResponse.THERE_IS_ALREADY_MANAGER.getMessage(), response.getHeaders().getFirst("message"));
    }
}