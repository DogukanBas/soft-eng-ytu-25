package com.softeng.backend.controller;

import com.softeng.backend.dto.AccountantDTOs;
import com.softeng.backend.model.User;
import com.softeng.backend.service.BudgetByCostTypeService;
import com.softeng.backend.service.DepartmentService;
import com.softeng.backend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/accountant")
public class AccountantController {
    private static final Logger logger = LoggerFactory.getLogger(AccountantController.class);
    private final DepartmentService departmentService;
    private final BudgetByCostTypeService budgetByCostTypeService;
    private final UserService userService;

    @Autowired
    public AccountantController(DepartmentService departmentService, BudgetByCostTypeService budgetByCostTypeService, UserService userService) {
        this.departmentService = departmentService;
        this.budgetByCostTypeService = budgetByCostTypeService;
        this.userService = userService;
    }

    @PostMapping("departments/set-initial-budget")
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
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.SetBudgetResponse.BUDGET_SET.getMessage())
                .build();
    }

    @PostMapping("departments/set-remaining-budget")
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
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.SetBudgetResponse.BUDGET_SET.getMessage())
                .build();
    }

    @PostMapping("departments/reset-budget")
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
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.SetBudgetResponse.BUDGET_SET.getMessage())
                .build();
    }

    @PostMapping("cost-type/add")
    public ResponseEntity<?> addCostType(String costTypeName, Double initialBudget, Authentication authentication) {
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
        budgetByCostTypeService.addBudgetByCostType(costTypeName, initialBudget);
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.AddCostTypeResponse.COST_TYPE_ADDED.getMessage())
                .build();
    }

    @PostMapping("cost-type/set-initial-budget")
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
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.SetBudgetResponse.BUDGET_SET.getMessage())
                .build();
    }

    @PostMapping("cost-type/set-remaining-budget")
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
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.SetBudgetResponse.BUDGET_SET.getMessage())
                .build();
    }

    @PostMapping("cost-type/reset-budget")
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
        return ResponseEntity.ok()
                .header("message", AccountantDTOs.SetBudgetResponse.BUDGET_SET.getMessage())
                .build();
    }

    private boolean isAuthenticated(Authentication authentication) {
        String personalNo = authentication.getName();
        User currentUser = userService.getUserByPersonalNo(personalNo);
        logger.debug("Current User: {}", personalNo);
        return currentUser.getUserType() == User.UserType.accountant;
    }
}
