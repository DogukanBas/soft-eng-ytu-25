package com.softeng.backend.controller;

import com.softeng.backend.dto.TicketDTOs;
import com.softeng.backend.model.BudgetByCostType;
import com.softeng.backend.model.Department;
import com.softeng.backend.model.Ticket;
import com.softeng.backend.model.User;
import com.softeng.backend.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/ticket")
public class TicketController {
    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);
    private final TicketService ticketService;
    private final BudgetByCostTypeService budgetByCostTypeService;
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;
    private final UserService userService;
    @Autowired
    public TicketController(TicketService ticketService, BudgetByCostTypeService budgetByCostTypeService, DepartmentService departmentService, EmployeeService employeeService, UserService userService) {
        this.ticketService = ticketService;
        this.budgetByCostTypeService = budgetByCostTypeService;
        this.departmentService = departmentService;
        this.employeeService = employeeService;
        this.userService = userService;
    }

    @Transactional
    @PostMapping("/create-ticket")
    public ResponseEntity<?> createTicket(TicketDTOs.CreateTicketRequest request, Authentication authentication) {
        logger.debug("Creating new ticket with cost type: {}", request.getCostType());
        String personalNo = authentication.getName();
        User currentUser = userService.getUserByPersonalNo(personalNo);

        if (currentUser.getUserType() == User.UserType.team_member || currentUser.getUserType() == User.UserType.manager) {
            BigDecimal amount = request.getAmount();
            Department department = employeeService.getEmployeeByPersonalNo(personalNo).getDepartment();
            BudgetByCostType budgetByCostType = budgetByCostTypeService.getByTypeName(request.getCostType());
            BigDecimal departmentRemainingBudget = department.getRemainingBudget();
            BigDecimal costTypeRemainingBudget = budgetByCostType.getRemainingBudget();
            BigDecimal maxCost = budgetByCostType.getMaxCost();

            BigDecimal minCost = departmentRemainingBudget
                    .min(costTypeRemainingBudget)
                    .min(maxCost);

            if (amount.compareTo(minCost) < 0) { // amount < minCost

                return ResponseEntity.ok()
                        .body(new TicketDTOs.TicketResponse(
                                TicketDTOs.CreateTicketResponse.TICKET_CREATED.getMessage()
                        ));
            }
        }

        else {
            return ResponseEntity.status(403)
                    .body(new TicketDTOs.TicketResponse(
                            TicketDTOs.CreateTicketResponse.INVALID_AUTHENTICATION.getMessage()
                    ));
        }



    }



}
