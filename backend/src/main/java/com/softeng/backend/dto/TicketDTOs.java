package com.softeng.backend.dto;

import com.softeng.backend.model.ApproveHistory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TicketDTOs {

    public static class CreateTicketRequest {
        private String costType;
        private BigDecimal amount;
        private String description;
        private LocalDate date;
        private byte[] invoice;

        public String getCostType() {
            return costType;
        }

        public void setCostType(String costType) {
            this.costType = costType;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public byte[] getInvoice() {
            return invoice;
        }

        public void setInvoice(byte[] invoice) {
            this.invoice = invoice;
        }
    }

    public enum CreateTicketResponse {
        TICKET_CREATED("Ticket created successfully"),
        COST_EXCEEDS_MAX_COST("Cost exceeds maximum cost that can be claimed"),
        COST_EXCEEDS_DEPARTMENT_BUDGET("Cost exceeds remaining department budget"),
        COST_EXCEEDS_COST_TYPE_BUDGET("Cost exceeds remaining cost type budget"),
        DEPARTMENT_BUDGET_EMPTY("Department has no remaining budget"),
        COST_TYPE_BUDGET_EMPTY("Cost type has no remaining budget"),
        NO_MANAGER_AVAILABLE("No manager assigned to the department"),
        INVALID_AUTHENTICATION("Invalid authentication");

        private final String message;

        CreateTicketResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public enum EditTicketResponse {
        TICKET_EDITED("Ticket edited successfully"),
        COST_EXCEEDS_MAX_COST("Cost exceeds maximum cost that can be claimed"),
        COST_EXCEEDS_DEPARTMENT_BUDGET("Cost exceeds remaining department budget"),
        COST_EXCEEDS_COST_TYPE_BUDGET("Cost exceeds remaining cost type budget"),
        DEPARTMENT_BUDGET_EMPTY("Department has no remaining budget"),
        COST_TYPE_BUDGET_EMPTY("Cost type has no remaining budget");

        private final String message;

        EditTicketResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class TicketResponse {
        private String message;
        private BigDecimal budget;

        public TicketResponse(String message) {
            this.message = message;
        }

        public TicketResponse(String message, BigDecimal budget) {
            this.message = message;
            this.budget = budget;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public BigDecimal getBudget() {
            return budget;
        }
        public void setBudget(BigDecimal budget) {
            this.budget = budget;
        }
    }

    public static class TicketWithoutInvoiceResponse {
        private String costType;
        private BigDecimal amount;
        private String employeeId;
        private String managerId;

        public TicketWithoutInvoiceResponse(String costType, BigDecimal amount, String employeeId, String managerId) {
            this.costType = costType;
            this.amount = amount;
            this.employeeId = employeeId;
            this.managerId = managerId;
        }

        public String getCostType() {
            return costType;
        }

        public void setCostType(String costType) {
            this.costType = costType;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
        }

        public String getManagerId() {
            return managerId;
        }

        public void setManagerId(String managerId) {
            this.managerId = managerId;
        }
    }

    public static class ApproveHistoryResponse {
        private int id;
        private String status;
        private LocalDate date;
        private String actorId;
        private String actorRole;
        private String description;

        public ApproveHistoryResponse(ApproveHistory approveHistory) {
            this.id = approveHistory.getId();
            this.status = approveHistory.getStatus().name();
            this.date = approveHistory.getDate();
            this.actorId = approveHistory.getActor().getPersonalNo();
            this.description = approveHistory.getDescription();
            this.actorRole = approveHistory.getActor().getUserType().name();
        }

        public ApproveHistoryResponse(int id, String status, LocalDate date, String actorId, String actorRole, String description) {
            this.id = id;
            this.status = status;
            this.date = date;
            this.actorId = actorId;
            this.actorRole = actorRole;
            this.description = description;
        }

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public LocalDate getDate() {
            return date;
        }
        public void setDate(LocalDate date) {
            this.date = date;
        }
        public String getActorId() {
            return actorId;
        }
        public void setActorId(String actorId) {
            this.actorId = actorId;
        }
        public String getActorRole() {
            return actorRole;
        }
        public void setActorRole(String actorRole) {
            this.actorRole = actorRole;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class TicketActionRequest {
        private int ticketId;
        private String description;

        public TicketActionRequest(int ticketId, String comment) {
            this.ticketId = ticketId;
            this.description = comment;
        }

        public int getTicketId() {
            return ticketId;
        }

        public void setTicketId(int ticketId) {
            this.ticketId = ticketId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class EditTicketRequest {
        private int ticketId;
        private String costType;
        private BigDecimal amount;
        private String description;

        public EditTicketRequest(int ticketId, String costType, BigDecimal amount, String description) {
            this.ticketId = ticketId;
            this.costType = costType;
            this.amount = amount;
            this.description = description;
        }

        public int getTicketId() {
            return ticketId;
        }
        public void setTicketId(int ticketId) {
            this.ticketId = ticketId;
        }
        public String getCostType() {
            return costType;
        }
        public void setCostType(String costType) {
            this.costType = costType;
        }
        public BigDecimal getAmount() {
            return amount;
        }
        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class BudgetValidationResult {
        private final boolean isValid;
        private final String message;
        private final BigDecimal minCost;

        public BudgetValidationResult(boolean isValid, String message, BigDecimal minCost) {
            this.isValid = isValid;
            this.message = message;
            this.minCost = minCost;
        }

        public boolean isValid() { return isValid; }
        public String getMessage() { return message; }
        public BigDecimal getMinCost() { return minCost; }
    }
}