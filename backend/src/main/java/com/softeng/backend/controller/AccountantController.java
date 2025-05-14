package com.softeng.backend.controller;

import com.softeng.backend.dto.AccountantDTOs;
import com.softeng.backend.model.Notification;
import com.softeng.backend.model.User;
import com.softeng.backend.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/accountant")
public class AccountantController {
    private static final Logger logger = LoggerFactory.getLogger(AccountantController.class);
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;
    private final BudgetByCostTypeService budgetByCostTypeService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final ApproveHistoryService approveHistoryService;

    @Autowired
    public AccountantController(DepartmentService departmentService, EmployeeService employeeService, BudgetByCostTypeService budgetByCostTypeService, UserService userService, NotificationService notificationService, ApproveHistoryService approveHistoryService) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
        this.budgetByCostTypeService = budgetByCostTypeService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.approveHistoryService = approveHistoryService;
    }

    @PostMapping("/departments/set-initial-budget")
    public ResponseEntity<?> setDepartmentInitialBudget(String deptName, Double initialBudget, Authentication authentication) {
        logger.debug("Setting initial budget for department ID: {}", deptName);
        if (!departmentService.existsByDeptname(deptName)) {
            return ResponseEntity.status(404)
                    .header("message", AccountantDTOs.SetBudgetResponse.DEPARTMENT_NOT_FOUND.getMessage())
                    .build();
        }
        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(403)
                    .header("message", AccountantDTOs.SetBudgetResponse.INVALID_AUTHENTICATION.getMessage())
                    .build();
        }
        departmentService.setDepartmentInitialBudget(deptName, initialBudget);
        notificationService.createNotification(Notification.NotificationType.DEPARTMENT,
                "Initial budget set for department: " + deptName + " with amount: " + initialBudget,
                deptName);
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.SetBudgetResponse.BUDGET_SET.getMessage())
                .build();
    }

    @PostMapping("/departments/set-remaining-budget")
    public ResponseEntity<?> setDepartmentRemainingBudget(String deptName, Double remainingBudget, Authentication authentication) {
        logger.debug("Setting remaining budget for department ID: {}", deptName);
        if (!departmentService.existsByDeptname(deptName)) {
            return ResponseEntity.status(404)
                    .header("message", AccountantDTOs.SetBudgetResponse.DEPARTMENT_NOT_FOUND.getMessage())
                    .build();
        }
        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(403)
                    .header("message", AccountantDTOs.SetBudgetResponse.INVALID_AUTHENTICATION.getMessage())
                    .build();
        }
        departmentService.setDepartmentRemainingBudget(deptName, remainingBudget);
        notificationService.createNotification(Notification.NotificationType.DEPARTMENT,
                "Remaining budget set for department: " + deptName + " with amount: " + remainingBudget,
                deptName);
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.SetBudgetResponse.BUDGET_SET.getMessage())
                .build();
    }

    @PostMapping("/departments/reset-budget")
    public ResponseEntity<?> resetDepartmentBudget(String deptName, Authentication authentication) {
        logger.debug("Resetting budget for department ID: {}", deptName);
        if (!departmentService.existsByDeptname(deptName)) {
            return ResponseEntity.status(404)
                    .header("message", AccountantDTOs.SetBudgetResponse.DEPARTMENT_NOT_FOUND.getMessage())
                    .build();
        }
        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(403)
                    .header("message", AccountantDTOs.SetBudgetResponse.INVALID_AUTHENTICATION.getMessage())
                    .build();
        }
        departmentService.resetDepartmentBudget(deptName);
        notificationService.createNotification(Notification.NotificationType.DEPARTMENT,
                "Budget reset for department: " + deptName + " with amount: " + departmentService.getDepartmentByName(deptName).getInitialBudget(),
                deptName);
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.SetBudgetResponse.BUDGET_SET.getMessage())
                .build();
    }

    @PostMapping("/cost-types/add")
    public ResponseEntity<?> addCostType(String costTypeName, Double initialBudget, Double maxCost,  Authentication authentication) {
        logger.debug("Adding cost type: {}", costTypeName);
        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(403)
                    .header("message", AccountantDTOs.AddCostTypeResponse.INVALID_AUTHENTICATION.getMessage())
                    .build();
        }
        if (budgetByCostTypeService.existsByTypeName(costTypeName)) {
            return ResponseEntity.status(409)
                    .header("message", AccountantDTOs.AddCostTypeResponse.COST_TYPE_ALREADY_EXISTS.getMessage())
                    .build();
        }
        budgetByCostTypeService.addBudgetByCostType(costTypeName, initialBudget, maxCost);
        notificationService.createNotification(Notification.NotificationType.ALL,
                "New cost type added: " + costTypeName + "\n",
                null);
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.AddCostTypeResponse.COST_TYPE_ADDED.getMessage())
                .build();
    }

    @PostMapping("/cost-types/set-initial-budget")
    public ResponseEntity<?> setInitialBudgetByTypeName(String typeName, Double initialBudget, Authentication authentication) {
        logger.debug("Setting initial budget for cost type: {}", typeName);
        if (!budgetByCostTypeService.existsByTypeName(typeName)) {
            return ResponseEntity.status(404)
                    .header("message", AccountantDTOs.SetBudgetResponse.COST_TYPE_NOT_FOUND.getMessage())
                    .build();
        }
        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(403)
                    .header("message", AccountantDTOs.SetBudgetResponse.INVALID_AUTHENTICATION.getMessage())
                    .build();
        }
        budgetByCostTypeService.setInitialBudgetByTypeName(typeName, initialBudget);
        notificationService.createNotification(Notification.NotificationType.ALL,
                "Initial budget set for cost type: " + typeName + " with amount: " + initialBudget,
                null);
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.SetBudgetResponse.BUDGET_SET.getMessage())
                .build();
    }

    @PostMapping("/cost-types/set-remaining-budget")
    public ResponseEntity<?> setRemainingBudgetByTypeName(String typeName, Double remainingBudget, Authentication authentication) {
        logger.debug("Setting remaining budget for cost type: {}", typeName);
        if (!budgetByCostTypeService.existsByTypeName(typeName)) {
            return ResponseEntity.status(404)
                    .header("message", AccountantDTOs.SetBudgetResponse.COST_TYPE_NOT_FOUND.getMessage())
                    .build();
        }
        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(403)
                    .header("message", AccountantDTOs.SetBudgetResponse.INVALID_AUTHENTICATION.getMessage())
                    .build();
        }
        budgetByCostTypeService.setRemainingBudgetByTypeName(typeName, remainingBudget);
        notificationService.createNotification(Notification.NotificationType.ALL,
                "Remaining budget set for cost type: " + typeName + " with amount: " + remainingBudget,
                null);
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.SetBudgetResponse.BUDGET_SET.getMessage())
                .build();
    }

    @PostMapping("/cost-types/reset-budget")
    public ResponseEntity<?> resetBudgetByTypeName(String typeName, Authentication authentication) {
        logger.debug("Resetting budget for cost type: {}", typeName);
        if (!budgetByCostTypeService.existsByTypeName(typeName)) {
            return ResponseEntity.status(404)
                    .header("message", AccountantDTOs.SetBudgetResponse.COST_TYPE_NOT_FOUND.getMessage())
                    .build();
        }
        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(403)
                    .header("message", AccountantDTOs.SetBudgetResponse.INVALID_AUTHENTICATION.getMessage())
                    .build();
        }
        budgetByCostTypeService.resetBudgetByTypeName(typeName);
        notificationService.createNotification(Notification.NotificationType.ALL,
                "Budget reset for cost type: " + typeName + " with amount: " + budgetByCostTypeService.getByTypeName(typeName).getRemainingBudget(),
                null);
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.SetBudgetResponse.BUDGET_SET.getMessage())
                .build();
    }

    @PostMapping("/cost-types/set-max-cost")
    public ResponseEntity<?> setMaxCostByTypeName(String typeName, Double maxCost, Authentication authentication) {
        logger.debug("Setting max budget for cost type: {}", typeName);
        if (!budgetByCostTypeService.existsByTypeName(typeName)) {
            return ResponseEntity.status(404)
                    .header("message", AccountantDTOs.SetBudgetResponse.COST_TYPE_NOT_FOUND.getMessage())
                    .build();
        }
        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(403)
                    .header("message", AccountantDTOs.SetBudgetResponse.INVALID_AUTHENTICATION.getMessage())
                    .build();
        }
        budgetByCostTypeService.setMaxCostByTypeName(typeName, maxCost);
        notificationService.createNotification(Notification.NotificationType.ALL,
                "Max cost set for cost type: " + typeName + " with amount: " + maxCost,
                null);
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.SetBudgetResponse.BUDGET_SET.getMessage())
                .build();
    }

    @GetMapping("/cost-types")
    public ResponseEntity<?> getAllCostTypes(Authentication authentication) {
        logger.debug("Getting all cost types in JSON format");
        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(403)
                    .header("message", AccountantDTOs.GetBudgetResponse.INVALID_AUTHENTICATION.getMessage())
                    .build();
        }
        List<AccountantDTOs.BudgetResponse> budgetResponses = budgetByCostTypeService.getAllCostTypes()
                .stream()
                .map(AccountantDTOs.BudgetResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("costTypes",
                budgetResponses
        ));
    }

    @GetMapping("/departments")
    public ResponseEntity<?> getAllDepartments(Authentication authentication) {
        logger.debug("Getting all departments in JSON format");
        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(403)
                    .header("message", AccountantDTOs.GetBudgetResponse.INVALID_AUTHENTICATION.getMessage())
                    .build();
        }

        List<AccountantDTOs.BudgetResponse> budgetResponses = departmentService.getAllDepartments()
                .stream()
                .map(AccountantDTOs.BudgetResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of("departments",
                budgetResponses
        ));
    }

    @GetMapping("/entities")
    public ResponseEntity<?> getEntities(@RequestParam String query, Authentication authentication) {
        logger.debug("Getting entities for query: {}", query);
        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(403)
                    .header("message", AccountantDTOs.GetBudgetResponse.INVALID_AUTHENTICATION.getMessage())
                    .build();
        }

        List<AccountantDTOs.EntityResponse> responses;
        switch (query.toLowerCase()) {
            case "employee":
                responses = employeeService.getAllEmployees().stream()
                    .map(emp -> new AccountantDTOs.EntityResponse(
                        emp.getPersonalNo(),
                        emp.getName() + " " + emp.getSurname()
                    ))
                    .collect(Collectors.toList());
                break;
            case "department":
                responses = departmentService.getAllDepartments().stream()
                    .map(dept -> new AccountantDTOs.EntityResponse(
                        dept.getDeptId().toString(),
                        dept.getDeptname()
                    ))
                    .collect(Collectors.toList());
                break;
            case "costtype":
                responses = budgetByCostTypeService.getAllCostTypes().stream()
                    .map(cost -> new AccountantDTOs.EntityResponse(
                        cost.getId().toString(),
                        cost.getTypeName()
                    ))
                    .collect(Collectors.toList());
                break;
            default:
                return ResponseEntity.status(400)
                    .header("message", "Invalid query type. Must be one of: Employee, Department, Costtype")
                    .build();
        }

        return ResponseEntity.ok(Map.of("entities", responses));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam String type, @RequestParam String id, Authentication authentication) {
        logger.debug("Getting stats for type: {} and id: {}", type, id);
        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(403)
                    .header("message", AccountantDTOs.GetBudgetResponse.INVALID_AUTHENTICATION.getMessage())
                    .build();
        }

        List<AccountantDTOs.StatEntry> stats;
        switch (type.toLowerCase()) {
            case "employee":
                if (!employeeService.existsByPersonalNo(id)) {
                    return ResponseEntity.status(404)
                            .header("message", "Employee not found")
                            .build();
                }
                stats = approveHistoryService.getApprovedExpensesByEmployee(id);
                break;
                
            case "department":
                Integer deptId;
                try {
                    deptId = Integer.parseInt(id);
                } catch (NumberFormatException e) {
                    return ResponseEntity.status(400)
                            .header("message", "Invalid department ID format")
                            .build();
                }
                
                if (!departmentService.existsByDeptId(deptId)) {
                    return ResponseEntity.status(404)
                            .header("message", "Department not found")
                            .build();
                }
                stats = approveHistoryService.getApprovedExpensesByDepartment(deptId);
                break;
                
            case "costtype":
                if (!budgetByCostTypeService.existsByTypeName(id)) {
                    return ResponseEntity.status(404)
                            .header("message", "Cost type not found")
                            .build();
                }
                stats = approveHistoryService.getApprovedExpensesByCostType(id);
                break;
                
            default:
                return ResponseEntity.status(400)
                    .header("message", "Invalid type. Must be one of: Employee, Department, Costtype")
                    .build();
        }
        return ResponseEntity.ok(Map.of("stats", stats));
    }

    private boolean isAuthenticated(Authentication authentication) {
        String personalNo = authentication.getName();
        User currentUser = userService.getUserByPersonalNo(personalNo);
        logger.debug("Current User: {}", personalNo);
        return currentUser.getUserType() == User.UserType.accountant;
    }
}
