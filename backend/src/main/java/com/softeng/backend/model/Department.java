package com.softeng.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "departments")
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer deptId;

    @Column(nullable = false)
    private String deptname;

    @Column(columnDefinition = "NUMERIC(12,2) DEFAULT 0")
    private BigDecimal remainingBudget;

    @Column(columnDefinition = "NUMERIC(12,2) DEFAULT 0")
    private BigDecimal initialBudget;

    @OneToOne
    @JoinColumn(name="deptManager", referencedColumnName = "personalNo", columnDefinition = "VARCHAR(20)")
    private User deptManager;

    public Department() {
        this.remainingBudget = BigDecimal.ZERO;
        this.initialBudget = BigDecimal.ZERO;
    }

    public Department(String deptname) {
        this.deptname = deptname;
        this.remainingBudget = BigDecimal.ZERO;
        this.initialBudget = BigDecimal.ZERO;
    }
}