package com.softeng.backend.dto;

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
}