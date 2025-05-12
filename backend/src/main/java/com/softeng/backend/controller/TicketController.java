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
import java.time.LocalDate;
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
    private final NotificationService notificationService;
    @Autowired
    public TicketController(TicketService ticketService, BudgetByCostTypeService budgetByCostTypeService, DepartmentService departmentService, EmployeeService employeeService, UserService userService, NotificationService notificationService) {
        this.ticketService = ticketService;
        this.budgetByCostTypeService = budgetByCostTypeService;
        this.departmentService = departmentService;
        this.employeeService = employeeService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Transactional
    @PostMapping("/create-ticket")
    public ResponseEntity<?> createTicket(@RequestBody TicketDTOs.CreateTicketRequest request, Authentication authentication) {
        logger.debug("Creating new ticket with cost type: {}", request.getCostType());
        String personalNo = authentication.getName();
        User currentUser = userService.getUserByPersonalNo(personalNo);

        if (currentUser.getUserType() == User.UserType.team_member || currentUser.getUserType() == User.UserType.manager) {
            Department department = employeeService.getEmployeeByPersonalNo(personalNo).getDepartment();
            if (department.getDeptManager() == null) {
                return ResponseEntity.status(403)
                        .header("message", TicketDTOs.CreateTicketResponse.NO_MANAGER_AVAILABLE.getMessage())
                        .build();
            }

            Ticket ticket = new Ticket(personalNo,
                    department.getDeptManager().getPersonalNo(),
                    request.getCostType(),
                    request.getAmount()
            );

            ResponseEntity<?> updateResult = processTicketUpdate(ticket, request.getCostType(),
                request.getAmount(), request.getDescription(), authentication, 
                currentUser.getUserType() == User.UserType.team_member ? ApproveHistory.Status.SENT_TO_MANAGER : ApproveHistory.Status.SENT_TO_ACCOUNTANT);

            if (updateResult.getStatusCode().is2xxSuccessful()) {
                Attachment attachment = new Attachment();
                attachment.setTicket(ticket);
                attachment.setInvoice(request.getInvoice());
                ticketService.addAttachment(attachment);
            }

            if (currentUser.getUserType() == User.UserType.team_member) {
                notificationService.createNotification(Notification.NotificationType.EMPLOYEE,
                        "New ticket created by " + personalNo,
                        department.getDeptManager().getPersonalNo());
            } else {
                notificationService.createNotification(Notification.NotificationType.ACCOUNTANT,
                        "New ticket created by " + personalNo,
                        null);

            }

            return updateResult;
        }

        return ResponseEntity.status(403)
                .header("message", TicketDTOs.CreateTicketResponse.INVALID_AUTHENTICATION.getMessage())
                .build();
    }

    @PostMapping("/edit")
    @Transactional
    public ResponseEntity<?> editTicket(@RequestBody TicketDTOs.EditTicketRequest editTicketRequest, Authentication authentication) {
        logger.debug("Editing ticket with ID: {}", editTicketRequest.getTicketId());
        ApproveHistory lastApproveHistory = ticketService.getLastApproveHistoryByTicketId(editTicketRequest.getTicketId());
        String personalNo = authentication.getName();
        if (isTeamMember(authentication)) {
            boolean isEditable = ApproveHistory.Status.getTeamMemberEditableStatus().contains(lastApproveHistory.getStatus());
            if (isEditable && lastApproveHistory.getTicket().getEmployeeId().equals(personalNo)) {
                return processTicketUpdate(lastApproveHistory.getTicket(), editTicketRequest.getCostType(), 
                    editTicketRequest.getAmount(), editTicketRequest.getDescription(), authentication, ApproveHistory.Status.SENT_TO_MANAGER);
            }
        } else if (isManager(authentication)) {
            boolean isEditable = ApproveHistory.Status.getManagerEditableStatus().contains(lastApproveHistory.getStatus());
            if (isEditable && lastApproveHistory.getTicket().getManagerId().equals(personalNo)) {
                return processTicketUpdate(lastApproveHistory.getTicket(), editTicketRequest.getCostType(), 
                    editTicketRequest.getAmount(), editTicketRequest.getDescription(), authentication, ApproveHistory.Status.SENT_TO_ACCOUNTANT);
            }
        }

        return ResponseEntity.status(403)
                .header("message", "Invalid authentication")
                .build();
    }

    @PostMapping("/edit-cost-type")
    @Transactional
    public ResponseEntity<?> editCostType(@RequestParam int ticketId, @RequestParam String costType, Authentication authentication) {
        logger.debug("Editing cost type: {}", ticketId);
        ApproveHistory lastApproveHistory = ticketService.getLastApproveHistoryByTicketId(ticketId);
        if (isAccountant(authentication)) {
            boolean isEditable = ApproveHistory.Status.getAccountantEditableStatus().contains(lastApproveHistory.getStatus());
            if (isEditable) {
                return processTicketUpdate(lastApproveHistory.getTicket(), costType, lastApproveHistory.getTicket().getAmount(),
                    "Cost Type Edited", authentication, ApproveHistory.Status.SENT_TO_ACCOUNTANT);
            }
        }

        return ResponseEntity.status(403)
                .header("message", "Invalid authentication")
                .build();
    }

    private ResponseEntity<?> processTicketUpdate(Ticket ticket, String costType, BigDecimal requestedAmount, 
            String description, Authentication authentication, ApproveHistory.Status nextStatus) {
        
        TicketDTOs.BudgetValidationResult validationResult = validateBudget(ticket.getEmployeeId(), costType, requestedAmount);
        if (!validationResult.isValid()) {
            return ResponseEntity.status(403)
                    .header("message", validationResult.getMessage())
                    .build();
        }

        ticket.setCostType(costType);
        ticket.setAmount(validationResult.getMinCost());
        ticketService.addTicket(ticket);

        ApproveHistory approveHistory = new ApproveHistory(ticket,
                LocalDate.now(),
                nextStatus,
                description,
                userService.getUserByPersonalNo(authentication.getName())
        );
        ticketService.addApproveHistory(approveHistory);

        return ResponseEntity.ok()
                .body(new TicketDTOs.TicketResponse(
                        validationResult.getMessage(),
                        validationResult.getMinCost()
                ));
    }

    private TicketDTOs.BudgetValidationResult validateBudget(String employeeId, String costType, BigDecimal requestedAmount) {
        BudgetByCostType budgetByCostType = budgetByCostTypeService.getByTypeName(costType);
        BigDecimal departmentRemainingBudget = employeeService.getEmployeeByPersonalNo(employeeId)
                .getDepartment().getRemainingBudget();
        BigDecimal costTypeRemainingBudget = budgetByCostType.getRemainingBudget();
        BigDecimal maxCost = budgetByCostType.getMaxCost();

        StringJoiner messageJoiner = new StringJoiner("\n");
        boolean isEmpty = false;

        if (departmentRemainingBudget.compareTo(BigDecimal.ZERO) <= 0) {
            messageJoiner.add(TicketDTOs.CreateTicketResponse.DEPARTMENT_BUDGET_EMPTY.getMessage());
            isEmpty = true;
        }

        if (costTypeRemainingBudget.compareTo(BigDecimal.ZERO) <= 0) {
            messageJoiner.add(TicketDTOs.CreateTicketResponse.COST_TYPE_BUDGET_EMPTY.getMessage());
            isEmpty = true;
        }

        if (isEmpty) {
            return new TicketDTOs.BudgetValidationResult(false, messageJoiner.toString(), BigDecimal.ZERO);
        }

        if (requestedAmount.compareTo(departmentRemainingBudget) > 0) {
            messageJoiner.add(TicketDTOs.CreateTicketResponse.COST_EXCEEDS_DEPARTMENT_BUDGET.getMessage());
        }

        if (requestedAmount.compareTo(costTypeRemainingBudget) > 0) {
            messageJoiner.add(TicketDTOs.CreateTicketResponse.COST_EXCEEDS_COST_TYPE_BUDGET.getMessage());
        }

        if (requestedAmount.compareTo(maxCost) > 0) {
            messageJoiner.add(TicketDTOs.CreateTicketResponse.COST_EXCEEDS_MAX_COST.getMessage());
        }

        BigDecimal minCost = departmentRemainingBudget
                .min(costTypeRemainingBudget)
                .min(maxCost)
                .min(requestedAmount);

        return new TicketDTOs.BudgetValidationResult(true, messageJoiner.toString(), minCost);
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

    @GetMapping("/approve-history")
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

    @PostMapping("/approve")
    @Transactional
    public ResponseEntity<?> approveTicket(@RequestBody TicketDTOs.TicketActionRequest ticketActionRequest, Authentication authentication) {
        logger.debug("Approving ticket with ID: {}", ticketActionRequest.getTicketId());
        String personalNo = authentication.getName();
        ApproveHistory lastApproveHistory = ticketService.getLastApproveHistoryByTicketId(ticketActionRequest.getTicketId());
        if (isManager(authentication) && lastApproveHistory.getStatus().equals(ApproveHistory.Status.SENT_TO_MANAGER) && lastApproveHistory.getTicket().getManagerId().equals(personalNo)) {
            ApproveHistory approveHistory = new ApproveHistory(lastApproveHistory.getTicket(),
                    LocalDate.now(),
                    ApproveHistory.Status.SENT_TO_ACCOUNTANT,
                    ticketActionRequest.getDescription(),
                    userService.getUserByPersonalNo(authentication.getName())
                    );
            ticketService.addApproveHistory(approveHistory);
            notificationService.createNotification(Notification.NotificationType.EMPLOYEE,
                    "Ticket approved by manager: " + employeeService.getEmployeeByPersonalNo(personalNo).getName() + " " +
                            employeeService.getEmployeeByPersonalNo(personalNo).getSurname(),
                    lastApproveHistory.getTicket().getEmployeeId());
            return ResponseEntity.ok()
                    .body(Map.of("message", "Ticket approved and sent to accountant"));

        }
        else if (isAccountant(authentication) && lastApproveHistory.getStatus().equals(ApproveHistory.Status.SENT_TO_ACCOUNTANT)) {
            TicketDTOs.TicketWithoutInvoiceResponse ticket = ticketService.getTicketById(ticketActionRequest.getTicketId());
            TicketDTOs.BudgetValidationResult validationResult = validateBudget(ticket.getEmployeeId(), ticket.getCostType(), ticket.getAmount());
            String description = ticketActionRequest.getDescription() + "\n" +
                    "Afforded Amount: " + validationResult.getMinCost() + "\n";

            if (validationResult.isValid()) {
                Department department = employeeService.getEmployeeByPersonalNo(ticket.getEmployeeId()).getDepartment();
                department.setRemainingBudget(department.getRemainingBudget().subtract(validationResult.getMinCost()));

                BudgetByCostType budgetByCostType = budgetByCostTypeService.getByTypeName(ticket.getCostType());
                budgetByCostType.setRemainingBudget(budgetByCostType.getRemainingBudget().subtract(validationResult.getMinCost()));

                if (budgetByCostType.getRemainingBudget().compareTo(BigDecimal.ZERO) <= 0) {
                    notificationService.createNotification(Notification.NotificationType.ALL,
                            "No remaining budget for cost type: " + ticket.getCostType(),
                            null);
                }

                if (department.getRemainingBudget().compareTo(BigDecimal.ZERO) <= 0) {
                    notificationService.createNotification(Notification.NotificationType.DEPARTMENT,
                            "No remaining budget for department: " + department.getDeptname(),
                            department.getDeptId().toString());
                }
            }

            ApproveHistory approveHistory = new ApproveHistory(lastApproveHistory.getTicket(),
                    LocalDate.now(),
                    ApproveHistory.Status.CLOSED_AS_APPROVED,
                    description,
                    userService.getUserByPersonalNo(authentication.getName())
            );
            ticketService.addApproveHistory(approveHistory);
            notificationService.createNotification(Notification.NotificationType.EMPLOYEE,
                    "Ticket approved by accountant: " + employeeService.getEmployeeByPersonalNo(personalNo).getName() + " " +
                            employeeService.getEmployeeByPersonalNo(personalNo).getSurname(),
                    lastApproveHistory.getTicket().getEmployeeId());
            return ResponseEntity.ok()
                    .body(Map.of("message", "Ticket approved and closed\n" +
                            "Afforded Amount: " + validationResult.getMinCost()));
        }

        else {
            return ResponseEntity.status(403)
                    .header("message",
                            "Invalid authentication"
                    )
                    .build();
        }
    }

    @PostMapping("/reject-and-return")
    @Transactional
    public ResponseEntity<?> rejectAndReturn(@RequestBody TicketDTOs.TicketActionRequest ticketActionRequest, Authentication authentication) {
        logger.debug("Rejecting and returning ticket with ID: {}", ticketActionRequest.getTicketId());
        ApproveHistory lastApproveHistory = ticketService.getLastApproveHistoryByTicketId(ticketActionRequest.getTicketId());
        String personalNo = authentication.getName();
        if (isManager(authentication) && lastApproveHistory.getStatus().equals(ApproveHistory.Status.SENT_TO_MANAGER) && lastApproveHistory.getTicket().getManagerId().equals(personalNo)) {
            ApproveHistory approveHistory = new ApproveHistory(lastApproveHistory.getTicket(),
                    LocalDate.now(),
                    ApproveHistory.Status.REJECTED_BY_MANAGER_CAN_BE_FIXED,
                    ticketActionRequest.getDescription(),
                    userService.getUserByPersonalNo(authentication.getName())
            );
            ticketService.addApproveHistory(approveHistory);
            notificationService.createNotification(Notification.NotificationType.EMPLOYEE,
                    "Ticket rejected and returned to fix by manager: " + employeeService.getEmployeeByPersonalNo(personalNo).getName() + " " +
                            employeeService.getEmployeeByPersonalNo(personalNo).getSurname(),
                    lastApproveHistory.getTicket().getEmployeeId());
            return ResponseEntity.ok()
                    .body(Map.of("message", "Ticket rejected and returned to employee"));
        }
        else if (isAccountant(authentication) && lastApproveHistory.getStatus().equals(ApproveHistory.Status.SENT_TO_ACCOUNTANT)) {
            ApproveHistory approveHistory = new ApproveHistory(lastApproveHistory.getTicket(),
                    LocalDate.now(),
                    ApproveHistory.Status.REJECTED_BY_ACCOUNTANT_CAN_BE_FIXED,
                    ticketActionRequest.getDescription(),
                    userService.getUserByPersonalNo(authentication.getName())
            );
            ticketService.addApproveHistory(approveHistory);
            notificationService.createNotification(Notification.NotificationType.EMPLOYEE,
                    "Ticket rejected and returned to fix by accountant: " + employeeService.getEmployeeByPersonalNo(personalNo).getName() + " " +
                            employeeService.getEmployeeByPersonalNo(personalNo).getSurname(),
                    lastApproveHistory.getTicket().getEmployeeId());
            return ResponseEntity.ok()
                    .body(Map.of("message", "Ticket rejected and returned to employee"));
        }
        else {
            return ResponseEntity.status(403)
                    .header("message",
                            "Invalid authentication"
                    )
                    .build();
        }

    }

    @PostMapping("/reject-and-close")
    @Transactional
    public ResponseEntity<?> rejectAndClose(@RequestBody TicketDTOs.TicketActionRequest ticketActionRequest, Authentication authentication) {
        logger.debug("Rejecting and closing ticket with ID: {}", ticketActionRequest.getTicketId());
        ApproveHistory lastApproveHistory = ticketService.getLastApproveHistoryByTicketId(ticketActionRequest.getTicketId());
        String personalNo = authentication.getName();
        if (isManager(authentication) && lastApproveHistory.getStatus().equals(ApproveHistory.Status.SENT_TO_MANAGER) && lastApproveHistory.getTicket().getManagerId().equals(personalNo)) {
            ApproveHistory approveHistory = new ApproveHistory(lastApproveHistory.getTicket(),
                    LocalDate.now(),
                    ApproveHistory.Status.CLOSED_AS_REJECTED_BY_MANAGER,
                    ticketActionRequest.getDescription(),
                    userService.getUserByPersonalNo(authentication.getName())
            );
            ticketService.addApproveHistory(approveHistory);
            notificationService.createNotification(Notification.NotificationType.EMPLOYEE,
                    "Ticket rejected and closed by manager: " + employeeService.getEmployeeByPersonalNo(personalNo).getName() + " " +
                            employeeService.getEmployeeByPersonalNo(personalNo).getSurname(),
                    lastApproveHistory.getTicket().getEmployeeId());
            return ResponseEntity.ok()
                    .body(Map.of("message", "Ticket rejected and closed"));
        }
        else if (isAccountant(authentication) && lastApproveHistory.getStatus().equals(ApproveHistory.Status.SENT_TO_ACCOUNTANT)) {
            ApproveHistory approveHistory = new ApproveHistory(lastApproveHistory.getTicket(),
                    LocalDate.now(),
                    ApproveHistory.Status.CLOSED_AS_REJECTED_BY_ACCOUNTANT,
                    ticketActionRequest.getDescription(),
                    userService.getUserByPersonalNo(authentication.getName())
            );
            ticketService.addApproveHistory(approveHistory);
            notificationService.createNotification(Notification.NotificationType.EMPLOYEE,
                    "Ticket rejected and closed by accountant: " + employeeService.getEmployeeByPersonalNo(personalNo).getName() + " " +
                            employeeService.getEmployeeByPersonalNo(personalNo).getSurname(),
                    lastApproveHistory.getTicket().getEmployeeId());
            return ResponseEntity.ok()
                    .body(Map.of("message", "Ticket rejected and closed"));
        }
        else {
            return ResponseEntity.status(403)
                    .header("message",
                            "Invalid authentication"
                    )
                    .build();
        }
    }

    @PostMapping("/cancel")
    @Transactional
    public ResponseEntity<?> cancelTicket(@RequestBody TicketDTOs.TicketActionRequest ticketActionRequest, Authentication authentication) {
        logger.debug("Canceling ticket with ID: {}", ticketActionRequest.getTicketId());
        ApproveHistory lastApproveHistory = ticketService.getLastApproveHistoryByTicketId(ticketActionRequest.getTicketId());
        String personalNo = authentication.getName();
        if (isTeamMember(authentication)) {
            boolean isCancelable = ApproveHistory.Status.getTeamMemberCancelableStatus().contains(lastApproveHistory.getStatus());
            if (isCancelable && lastApproveHistory.getTicket().getEmployeeId().equals(personalNo)) {
                ApproveHistory approveHistory = new ApproveHistory(lastApproveHistory.getTicket(),
                        LocalDate.now(),
                        ApproveHistory.Status.CANCELED_BY_USER,
                        ticketActionRequest.getDescription(),
                        userService.getUserByPersonalNo(authentication.getName())
                );
                ticketService.addApproveHistory(approveHistory);
                return ResponseEntity.ok()
                        .body(Map.of("message", "Ticket canceled"));
            }
            else {
                return ResponseEntity.status(403)
                        .header("message",
                                "Invalid authentication"
                        )
                        .build();
            }
        }
        else if (isManager(authentication)) {
            boolean isCancelable = ApproveHistory.Status.getManagerCancelableStatus().contains(lastApproveHistory.getStatus());
            if (isCancelable && lastApproveHistory.getTicket().getManagerId().equals(personalNo)) {
                ApproveHistory approveHistory = new ApproveHistory(lastApproveHistory.getTicket(),
                        LocalDate.now(),
                        ApproveHistory.Status.CANCELED_BY_USER,
                        ticketActionRequest.getDescription(),
                        userService.getUserByPersonalNo(authentication.getName())
                );
                ticketService.addApproveHistory(approveHistory);
                return ResponseEntity.ok()
                        .body(Map.of("message", "Ticket canceled"));
            }
            else {
                return ResponseEntity.status(403)
                        .header("message",
                                "Invalid authentication"
                        )
                        .build();
            }
        }
        else {
            return ResponseEntity.status(403)
                    .header("message",
                            "Invalid authentication"
                    )
                    .build();
        }
    }

    @GetMapping("/invoice")
    public ResponseEntity<?> getInvoice(@RequestParam int ticketId, Authentication authentication) {
        logger.debug("Fetching invoice for ticket with ID: {}", ticketId);
        String personalNo = authentication.getName();
        TicketDTOs.TicketWithoutInvoiceResponse ticket = ticketService.getTicketById(ticketId);
        if ( isAccountant(authentication)
            || isManager(authentication) && (personalNo.equals(ticket.getEmployeeId()) || personalNo.equals(ticket.getManagerId()))
            || isTeamMember(authentication) && personalNo.equals(ticket.getEmployeeId())) {
            Attachment attachment = ticketService.getAttachmentByTicketId(ticketId);
            if (attachment != null) {
                return ResponseEntity.ok()
                        .body(Map.of("invoice", attachment.getInvoice()));
            } else {
                return ResponseEntity.status(404)
                        .header("message", "Invoice not found").build();
            }
        } else {
            return ResponseEntity.status(403)
                    .header("message",
                            "Invalid authentication"
                    )
                    .build();
        }
    }

    private boolean isTeamMember(Authentication authentication) {
        String personalNo = authentication.getName();
        User currentUser = userService.getUserByPersonalNo(personalNo);
        return currentUser.getUserType() == User.UserType.team_member;
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
