package com.softeng.backend.controller;

import com.softeng.backend.dto.AccountantDTOs;
import com.softeng.backend.model.BudgetByCostType;
import com.softeng.backend.model.User;
import com.softeng.backend.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AccountantControllerTest {

    @InjectMocks
    private AccountantController accountantController;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private BudgetByCostTypeService budgetByCostTypeService;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ApproveHistoryService approveHistoryService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCostType_success() {
        String costTypeName = "Meals";
        Double initialBudget = 1000.0;
        Double maxCost = 50.0;
        User accountantUser = new User("acc1", "acc@example.com", User.UserType.accountant);

        when(authentication.getName()).thenReturn("acc1");
        when(userService.getUserByPersonalNo("acc1")).thenReturn(accountantUser);
        when(budgetByCostTypeService.existsByTypeName(costTypeName)).thenReturn(false);

        ResponseEntity<?> response = accountantController.addCostType(costTypeName, initialBudget, maxCost, authentication);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(AccountantDTOs.AddCostTypeResponse.COST_TYPE_ADDED.getMessage(), response.getHeaders().getFirst("message"));
    }

    @Test
    void addCostType_alreadyExists() {
        String costTypeName = "Meals";
        Double initialBudget = 1000.0;
        Double maxCost = 50.0;
        User accountantUser = new User("acc1", "acc@example.com", User.UserType.accountant);

        when(authentication.getName()).thenReturn("acc1");
        when(userService.getUserByPersonalNo("acc1")).thenReturn(accountantUser);
        when(budgetByCostTypeService.existsByTypeName(costTypeName)).thenReturn(true);

        ResponseEntity<?> response = accountantController.addCostType(costTypeName, initialBudget, maxCost, authentication);

        assertEquals(409, response.getStatusCodeValue());
        assertEquals(AccountantDTOs.AddCostTypeResponse.COST_TYPE_ALREADY_EXISTS.getMessage(), response.getHeaders().getFirst("message"));
    }

    @Test
    void getAllCostTypes_success() {
        User accountantUser = new User("acc1", "acc@example.com", User.UserType.accountant);

        when(authentication.getName()).thenReturn("acc1");
        when(userService.getUserByPersonalNo("acc1")).thenReturn(accountantUser);
        when(budgetByCostTypeService.getAllCostTypes()).thenReturn(List.of(
                new BudgetByCostType("Travel", 1000.0, 500.0),
                new BudgetByCostType("Meals", 2000.0, 1500.0)
        ));

        ResponseEntity<?> response = accountantController.getAllCostTypes(authentication);

        assertEquals(200, response.getStatusCodeValue());
        Map<String, List<AccountantDTOs.BudgetResponse>> responseBody = (Map<String, List<AccountantDTOs.BudgetResponse>>) response.getBody();
        Assertions.assertNotNull(responseBody);
        assertEquals(2, responseBody.get("costTypes").size());
        assertEquals("Travel", responseBody.get("costTypes").get(0).getName());
        assertEquals("Meals", responseBody.get("costTypes").get(1).getName());
    }
}