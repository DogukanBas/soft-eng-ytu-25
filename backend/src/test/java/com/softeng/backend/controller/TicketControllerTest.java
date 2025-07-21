package com.softeng.backend.controller;

import com.softeng.backend.dto.TicketDTOs;
import com.softeng.backend.model.*;
import com.softeng.backend.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketControllerTest {

    @InjectMocks
    private TicketController ticketController;

    @Mock
    private TicketService ticketService;

    @Mock
    private BudgetByCostTypeService budgetByCostTypeService;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTicket_teamMember_success() {
        TicketDTOs.CreateTicketRequest request = new TicketDTOs.CreateTicketRequest();
        request.setCostType("Travel");
        request.setAmount(BigDecimal.valueOf(100));
        request.setDescription("Team travel");
        request.setDate(LocalDate.now());
        request.setInvoice(new byte[]{1, 2, 3});

        User teamMemberUser = new User("teamMember1", "tm@example.com", User.UserType.team_member);
        Department department = new Department("IT");
        Employee teamMemberEmployee = new Employee(teamMemberUser, "Test", "Member", department);
        teamMemberEmployee.getDepartment().setDeptManager(new User("manager1", "m@ex.com", User.UserType.manager)); // Ensure manager exists
        teamMemberEmployee.getDepartment().setRemainingBudget(BigDecimal.valueOf(1000));

        BudgetByCostType budgetByCostType = new BudgetByCostType("Travel", 200.0,200.0);
        budgetByCostType.setRemainingBudget(BigDecimal.valueOf(500));

        when(authentication.getName()).thenReturn("teamMember1");
        when(userService.getUserByPersonalNo("teamMember1")).thenReturn(teamMemberUser);
        when(employeeService.getEmployeeByPersonalNo("teamMember1")).thenReturn(teamMemberEmployee);
        when(budgetByCostTypeService.getByTypeName("Travel")).thenReturn(budgetByCostType);

        ResponseEntity<?> response = ticketController.createTicket(request, authentication);

        TicketDTOs.TicketResponse responseBody = (TicketDTOs.TicketResponse) response.getBody();
        Assertions.assertNotNull(responseBody);
        assertEquals(BigDecimal.valueOf(100), responseBody.getBudget());
    }

    @Test
    void approveTicket_manager_success() {
        TicketDTOs.TicketActionRequest request = new TicketDTOs.TicketActionRequest(1, "Approved by manager");
        User managerUser = new User("manager1", "m@example.com", User.UserType.manager);
        Ticket ticket = new Ticket("employee1", "manager1", "Travel", BigDecimal.valueOf(100));
        ticket.setTicketId(1);
        ApproveHistory lastApproveHistory = new ApproveHistory(ticket, LocalDate.now(), ApproveHistory.Status.SENT_TO_MANAGER, "Initial request", new User());

        when(authentication.getName()).thenReturn("manager1");
        when(userService.getUserByPersonalNo("manager1")).thenReturn(managerUser);
        when(ticketService.getLastApproveHistoryByTicketId(1)).thenReturn(lastApproveHistory);
        when(employeeService.getEmployeeByPersonalNo(anyString())).thenReturn(mock(Employee.class)); // Mock to avoid NPE

        ResponseEntity<?> response = ticketController.approveTicket(request, authentication);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Map.of("message", "Ticket approved and sent to accountant"), response.getBody());
    }
}