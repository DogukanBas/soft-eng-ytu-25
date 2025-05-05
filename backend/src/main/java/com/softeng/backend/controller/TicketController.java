package com.softeng.backend.controller;

import com.softeng.backend.dto.TicketDTOs;
import com.softeng.backend.model.*;
import com.softeng.backend.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

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
    public ResponseEntity<?> createTicket(@RequestBody TicketDTOs.CreateTicketRequest request, Authentication authentication) {
        logger.debug("Creating new ticket with cost type: {}", request.getCostType());
        String personalNo = authentication.getName();
        User currentUser = userService.getUserByPersonalNo(personalNo);

        if (currentUser.getUserType() == User.UserType.team_member || currentUser.getUserType() == User.UserType.manager) {
            BigDecimal amount = request.getAmount();
            Department department = employeeService.getEmployeeByPersonalNo(personalNo).getDepartment();
            if (department.getDeptManager()== null) {
                return ResponseEntity.status(403)
                        .header("message",
                                TicketDTOs.CreateTicketResponse.NO_MANAGER_AVAILABLE.getMessage()
                        )
                        .build();
            }
            BudgetByCostType budgetByCostType = budgetByCostTypeService.getByTypeName(request.getCostType());
            BigDecimal departmentRemainingBudget = department.getRemainingBudget();
            BigDecimal costTypeRemainingBudget = budgetByCostType.getRemainingBudget();
            BigDecimal maxCost = budgetByCostType.getMaxCost();

            boolean isEmpty = false;
            StringJoiner messageJoiner = new StringJoiner("\n");
            if (departmentRemainingBudget.compareTo(BigDecimal.ZERO) <= 0) {
                messageJoiner.add(TicketDTOs.CreateTicketResponse.DEPARTMENT_BUDGET_EMPTY.getMessage());
                isEmpty = true;
            }

            if (costTypeRemainingBudget.compareTo(BigDecimal.ZERO) <= 0) {
                messageJoiner.add(TicketDTOs.CreateTicketResponse.COST_TYPE_BUDGET_EMPTY.getMessage());
                isEmpty = true;
            }

            if (isEmpty) {
                return ResponseEntity.status(403)
                        .header("message",
                                messageJoiner.toString()
                        )
                        .build();
            }

            if (amount.compareTo(departmentRemainingBudget) > 0) {
                messageJoiner.add(TicketDTOs.CreateTicketResponse.COST_EXCEEDS_DEPARTMENT_BUDGET.getMessage());
            }

            if (amount.compareTo(costTypeRemainingBudget) > 0) {
                messageJoiner.add(TicketDTOs.CreateTicketResponse.COST_EXCEEDS_COST_TYPE_BUDGET.getMessage());
            }

            if (amount.compareTo(maxCost) > 0) {
                messageJoiner.add(TicketDTOs.CreateTicketResponse.COST_EXCEEDS_MAX_COST.getMessage());
            }

            BigDecimal minCost = departmentRemainingBudget
                    .min(costTypeRemainingBudget)
                    .min(maxCost)
                    .min(request.getAmount());

            Ticket ticket = new Ticket(personalNo,
                    department.getDeptManager().getPersonalNo(),
                    request.getCostType(),
                    minCost

            );
            ticketService.addTicket(ticket);

            Attachment attachment = new Attachment();
            attachment.setTicket(ticket);
            byte[] decodedInvoice = Base64.getDecoder().decode(request.getInvoice());
            attachment.setInvoice(decodedInvoice);
            ticketService.addAttachment(attachment);

            ApproveHistory approveHistory = new ApproveHistory();
            approveHistory.setTicket(ticket);
            approveHistory.setActor(currentUser);
            approveHistory.setDescription(request.getDescription());
            approveHistory.setDate(request.getDate());
            if (currentUser.getUserType() == User.UserType.team_member) {
                approveHistory.setStatus(ApproveHistory.Status.SENT_TO_MANAGER);
            } else {
                approveHistory.setStatus(ApproveHistory.Status.SENT_TO_ACCOUNTANT);
            }
            ticketService.addApproveHistory(approveHistory);

            messageJoiner.add(TicketDTOs.CreateTicketResponse.TICKET_CREATED.getMessage());
            return ResponseEntity.ok()
                    .body(new TicketDTOs.TicketResponse(
                            messageJoiner.toString(),
                            minCost
                    ));
        }
        else {
            return ResponseEntity.status(403)
                    .header("message",
                            TicketDTOs.CreateTicketResponse.INVALID_AUTHENTICATION.getMessage()
                    )
                    .build();
        }
    }

    @GetMapping("/cost-types")
    public ResponseEntity<?> getAllCostTypeNames() {
        logger.debug("Fetching cost types");
        List<String> costTypes = budgetByCostTypeService.getAllCostTypeNames();
        return ResponseEntity.ok()
                .body(Map.of("costTypes", costTypes));
    }

    @GetMapping("/created/closed")
    public ResponseEntity<?> getCreatedClosedTicketIDs(Authentication authentication) {
        logger.debug("Fetching ticket IDs");
        String personalNo = authentication.getName();
        List<Integer> ticketIds = ticketService.getCreatedClosedTicketIdsByPersonalNo(personalNo, isManager(authentication), isAccountant(authentication));
        if (ticketIds.isEmpty()) {
            return ResponseEntity.status(404)
                    .header("message","No Tickets found")
                    .build();
        }
        return ResponseEntity.ok()
                .body(Map.of("ticketIds", ticketIds));
    }

    @GetMapping("/assigned/closed")
    public ResponseEntity<?> getAssignedClosedTicketIDs(Authentication authentication) {
        logger.debug("Fetching assigned closed ticket IDs");
        String personalNo = authentication.getName();
        List<Integer> ticketIds = ticketService.getAssignedClosedTicketIdsByPersonalNo(personalNo, isManager(authentication), isAccountant(authentication));
        if (ticketIds.isEmpty()) {
            return ResponseEntity.status(404)
                    .header("message","No Tickets found")
                    .build();
        }
        return ResponseEntity.ok()
                .body(Map.of("ticketIds", ticketIds));
    }

    @GetMapping("/created/active")
    public ResponseEntity<?> getCreatedActiveTicketIDs(Authentication authentication) {
        logger.debug("Fetching created active ticket IDs");
        String personalNo = authentication.getName();
        List<Integer> ticketIds = ticketService.getCreatedActiveTicketIdsByPersonalNo(personalNo, isManager(authentication), isAccountant(authentication));
        if (ticketIds.isEmpty()) {
            return ResponseEntity.status(404)
                    .header("message","No Tickets found")
                    .build();
        }
        return ResponseEntity.ok()
                .body(Map.of("ticketIds", ticketIds));
    }

    @GetMapping("/assigned/active")
    public ResponseEntity<?> getAssignedActiveTicketIDs(Authentication authentication) {
        logger.debug("Fetching assigned active ticket IDs");
        String personalNo = authentication.getName();
        List<Integer> ticketIds = ticketService.getAssignedActiveTicketIdsByPersonalNo(personalNo, isManager(authentication), isAccountant(authentication));
        if (ticketIds.isEmpty()) {
            return ResponseEntity.status(404)
                    .header("message","No Tickets found")
                    .build();
        }
        return ResponseEntity.ok()
                .body(Map.of("ticketIds", ticketIds));
    }

    @GetMapping()
    public ResponseEntity<?> getTicketById(@RequestParam("ticketId") int ticketId) {
        logger.debug("Fetching ticket with ID: {}", ticketId);
        TicketDTOs.TicketWithoutInvoiceResponse ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "Ticket not found"));
        }
        return ResponseEntity.ok()
                .body(ticket);
    }

    @GetMapping
    ("/approve-history")
    public ResponseEntity<?> getApproveHistoryByTicketId(@RequestParam("ticketId") int ticketId) {
        logger.debug("Fetching approve history for ticket with ID: {}", ticketId);
        List<TicketDTOs.ApproveHistoryResponse> approveHistory = ticketService.getApproveHistoryByTicketId(ticketId);
        if (approveHistory.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "Approve history not found"));
        }
        return ResponseEntity.ok()
                .body(approveHistory);
    }

    private boolean isManager(Authentication authentication) {
        String personalNo = authentication.getName();
        User currentUser = userService.getUserByPersonalNo(personalNo);
        return currentUser.getUserType() == User.UserType.manager;
    }

    private boolean isAccountant(Authentication authentication) {
        String personalNo = authentication.getName();
        User currentUser = userService.getUserByPersonalNo(personalNo);
        return currentUser.getUserType() == User.UserType.accountant;
    }
}
