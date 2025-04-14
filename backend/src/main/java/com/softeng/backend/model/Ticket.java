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
    private Long ticketId;

    @ManyToOne
    @JoinColumn(name = "employeeId", nullable = false)
    private User employee;

    @ManyToOne
    @JoinColumn(name = "managerId", nullable = false)
    private User manager;

    @Column(nullable = false)
    private String costType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<ApproveHistory> history;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Attachment> attachments;
}