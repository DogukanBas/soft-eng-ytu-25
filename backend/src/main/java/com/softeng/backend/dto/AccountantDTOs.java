package com.softeng.backend.dto;

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
}
