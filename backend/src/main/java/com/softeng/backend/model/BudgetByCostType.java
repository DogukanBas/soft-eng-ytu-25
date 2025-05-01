package com.softeng.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "budgetByCostType")
@Data
public class BudgetByCostType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String typeName;

    @Column(columnDefinition = "NUMERIC(12,2) DEFAULT 0")
    private BigDecimal remainingBudget;

    @Column(columnDefinition = "NUMERIC(12,2) DEFAULT 0")
    private BigDecimal initialBudget;

    @Column(columnDefinition = "NUMERIC(12,2) DEFAULT 0")
    private BigDecimal maxCost;

    public BudgetByCostType() {
    }

    public BudgetByCostType(String typeName, Double initialBudget, Double maxCost) {
        this.typeName = typeName;
        this.initialBudget = BigDecimal.valueOf(initialBudget);
        this.maxCost = BigDecimal.valueOf(maxCost);
        this.remainingBudget = this.initialBudget;

    }
}