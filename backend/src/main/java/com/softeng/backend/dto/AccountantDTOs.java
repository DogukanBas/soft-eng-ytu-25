package com.softeng.backend.dto;

import com.softeng.backend.model.BudgetByCostType;
import com.softeng.backend.model.Department;

import java.math.BigDecimal;

public class AccountantDTOs {

    public enum AddCostTypeResponse {
        COST_TYPE_ADDED("Cost type added successfully"),
        COST_TYPE_ALREADY_EXISTS("Cost type already exists"),
        INVALID_AUTHENTICATION("Invalid authentication");

        private final String message;

        AddCostTypeResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public enum SetBudgetResponse {
        BUDGET_SET("Budget set successfully"),
        INVALID_AUTHENTICATION("Invalid authentication"),
        DEPARTMENT_NOT_FOUND("Department not found"),
        COST_TYPE_NOT_FOUND("Cost type not found");

        private final String message;

        SetBudgetResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }


    public enum GetBudgetResponse {
        INVALID_AUTHENTICATION("Invalid authentication");

        private final String message;

        GetBudgetResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class BudgetResponse {
        private String name;
        private BigDecimal initialBudget;
        private BigDecimal remainingBudget;
        private BigDecimal maxCost;

        public BudgetResponse(String typeName, BigDecimal initialBudget, BigDecimal remainingBudget) {
            this.name = typeName;
            this.initialBudget = initialBudget;
            this.remainingBudget = remainingBudget;
        }

        public BudgetResponse(Department department) {
            this.name = department.getDeptname();
            this.initialBudget = department.getInitialBudget();
            this.remainingBudget = department.getRemainingBudget();
        }

        public BudgetResponse(BudgetByCostType budgetByCostType) {
            this.name = budgetByCostType.getTypeName();
            this.initialBudget = budgetByCostType.getInitialBudget();
            this.remainingBudget = budgetByCostType.getRemainingBudget();
            this.maxCost = budgetByCostType.getMaxCost();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getInitialBudget() {
            return initialBudget;
        }

        public void setInitialBudget(BigDecimal initialBudget) {
            this.initialBudget = initialBudget;
        }

        public BigDecimal getRemainingBudget() {
            return remainingBudget;
        }

        public void setRemainingBudget(BigDecimal remainingBudget) {
            this.remainingBudget = remainingBudget;
        }
        public BigDecimal getMaxCost() {
            return maxCost;
        }
        public void setMaxCost(BigDecimal maxCost) {
            this.maxCost = maxCost;
        }
    }

    public static class EntityResponse {
        private String id;
        private String name;

        public EntityResponse(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class StatEntry {
        private String date;
        private Double amount;

        public StatEntry(String date, Double amount) {
            this.date = date;
            this.amount = amount;
        }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
    }
}
