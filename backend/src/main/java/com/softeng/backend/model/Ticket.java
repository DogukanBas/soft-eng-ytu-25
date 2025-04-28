package com.softeng.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tickets")
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticketId;

    @Column(nullable = false)
    private String employeeId;

    @Column(nullable = false)
    private String managerId;

    @Column(nullable = false)
    private String costType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<ApproveHistory> history;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Attachment attachment;
}