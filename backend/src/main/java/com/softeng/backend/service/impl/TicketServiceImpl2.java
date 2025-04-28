package com.softeng.backend.service.impl;

import com.softeng.backend.dto.TicketDTOs;
import com.softeng.backend.model.*;
import com.softeng.backend.repository.ApproveHistoryRepository;
import com.softeng.backend.repository.AttachmentRepository;
import com.softeng.backend.repository.TicketRepository;
import com.softeng.backend.service.BudgetByCostTypeService;
import com.softeng.backend.service.DepartmentService;
import com.softeng.backend.service.EmployeeService;
import com.softeng.backend.service.TicketService2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TicketServiceImpl2 implements TicketService2 {
    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl2.class);

    private final TicketRepository ticketRepository;
    private final AttachmentRepository attachmentRepository;
    private final ApproveHistoryRepository approveHistoryRepository;
    private final DepartmentService departmentService;
    private final BudgetByCostTypeService budgetByCostTypeService;
    private final EmployeeService employeeService;

    @Autowired
    public TicketServiceImpl2(TicketRepository ticketRepository,
                              AttachmentRepository attachmentRepository,
                              ApproveHistoryRepository approveHistoryRepository,
                              DepartmentService departmentService,
                              BudgetByCostTypeService budgetByCostTypeService,
                              EmployeeService employeeService) {
        this.ticketRepository = ticketRepository;
        this.attachmentRepository = attachmentRepository;
        this.approveHistoryRepository = approveHistoryRepository;
        this.departmentService = departmentService;
        this.budgetByCostTypeService = budgetByCostTypeService;
        this.employeeService = employeeService;
    }
/*
    @Override
    @Transactional
    public Map<String, Object> createTicket(String personalNo, TicketDTOs.CreateTicketRequest request) {
        // Get the employee
        Employee employee = employeeService.getEmployeeByPersonalNo(personalNo);
        Department department = employee.getDepartment();
        
        // Check if department has a manager
        if (department.getDeptManager() == null) {
            return Map.of(
                "status", TicketDTOs.CreateTicketResponse.NO_MANAGER_AVAILABLE,
                "success", false
            );
        }
        
        // Get cost type and check if it exists
        String costType = request.getCostType();
        if (!budgetByCostTypeService.existsByTypeName(costType)) {
            return Map.of(
                "status", TicketDTOs.CreateTicketResponse.COST_TYPE_NOT_FOUND,
                "success", false
            );
        }

        Optional<BudgetByCostType> budgetByCostTypeOpt = budgetByCostTypeService.getByTypeName(costType);
        if (!budgetByCostTypeOpt.isPresent()) {
            return Map.of(
                "status", TicketDTOs.CreateTicketResponse.COST_TYPE_NOT_FOUND,
                "success", false
            );
        }
        
        BudgetByCostType budgetByCostType = budgetByCostTypeOpt.get();
        BigDecimal amount = request.getAmount();
        
        // Check department budget
        BigDecimal deptRemainingBudget = department.getRemainingBudget();
        if (deptRemainingBudget.compareTo(BigDecimal.ZERO) == 0) {
            Map<String, Object> budgets = new HashMap<>();
            budgets.put("departmentBudget", deptRemainingBudget);
            budgets.put("costTypeBudget", budgetByCostType.getRemainingBudget());
            budgets.put("maxCost", budgetByCostType.getMaxCost());
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", TicketDTOs.CreateTicketResponse.DEPARTMENT_BUDGET_EMPTY);
            result.put("success", false);
            result.put("budgets", budgets);
            return result;
        }
        
        // Check cost type budget
        BigDecimal costTypeRemainingBudget = budgetByCostType.getRemainingBudget();
        if (costTypeRemainingBudget.compareTo(BigDecimal.ZERO) == 0) {
            Map<String, Object> budgets = new HashMap<>();
            budgets.put("departmentBudget", deptRemainingBudget);
            budgets.put("costTypeBudget", costTypeRemainingBudget);
            budgets.put("maxCost", budgetByCostType.getMaxCost());
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", TicketDTOs.CreateTicketResponse.COST_TYPE_BUDGET_EMPTY);
            result.put("success", false);
            result.put("budgets", budgets);
            return result;
        }
        
        // Check if amount exceeds department budget
        if (amount.compareTo(deptRemainingBudget) > 0) {
            Map<String, Object> budgets = new HashMap<>();
            budgets.put("departmentBudget", deptRemainingBudget);
            budgets.put("costTypeBudget", costTypeRemainingBudget);
            budgets.put("maxCost", budgetByCostType.getMaxCost());
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", TicketDTOs.CreateTicketResponse.COST_EXCEEDS_DEPARTMENT_BUDGET);
            result.put("success", false);
            result.put("budgets", budgets);
            return result;
        }
        
        // Check if amount exceeds cost type budget
        if (amount.compareTo(costTypeRemainingBudget) > 0) {
            Map<String, Object> budgets = new HashMap<>();
            budgets.put("departmentBudget", deptRemainingBudget);
            budgets.put("costTypeBudget", costTypeRemainingBudget);
            budgets.put("maxCost", budgetByCostType.getMaxCost());
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", TicketDTOs.CreateTicketResponse.COST_EXCEEDS_COST_TYPE_BUDGET);
            result.put("success", false);
            result.put("budgets", budgets);
            return result;
        }
        
        // Create ticket
        Ticket ticket = new Ticket();
        ticket.setEmployee(employee.getUser());
        ticket.setManager(department.getDeptManager());
        ticket.setCostType(costType);
        ticket.setAmount(amount);
        
        Ticket savedTicket = ticketRepository.save(ticket);
        
        // Create attachment if invoice is provided
        if (request.getInvoice() != null && request.getInvoice().length > 0) {
            Attachment attachment = new Attachment(savedTicket, request.getInvoice());
            attachmentRepository.save(attachment);
        }
        
        // Create ticket history
        ApproveHistory history = new ApproveHistory();
        history.setTicket(savedTicket);
        history.setDate(LocalDateTime.now());
        history.setStatus(ApproveHistory.Status.SENT_TO_MANAGER);
        history.setDescription(request.getDescription());
        history.setActor(employee.getUser());
        
        approveHistoryRepository.save(history);
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", TicketDTOs.CreateTicketResponse.TICKET_CREATED);
        result.put("success", true);
        result.put("ticketId", savedTicket.getTicketId());
        return result;
    }
     */
    @Override
    public List<Ticket> getTicketsByEmployee(User employee) {
        return ticketRepository.findByEmployee(employee);
    }
    
    @Override
    public List<Ticket> getTicketsByManager(User manager) {
        return ticketRepository.findByManager(manager);
    }
}