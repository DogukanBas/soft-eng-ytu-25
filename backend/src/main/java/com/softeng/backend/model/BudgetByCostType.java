package com.softeng.backend.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
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
}