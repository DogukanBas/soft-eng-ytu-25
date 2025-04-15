package com.softeng.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "approvehistory")
@Data
public class ApproveHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ticketId", nullable = false, referencedColumnName = "ticketId")
    private Ticket ticket;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String description;

    @ManyToOne
    @JoinColumn(name = "actorId", nullable = false, referencedColumnName = "personalNo")
    private User actor;

    public enum Status {
        SENT_TO_MANAGER("sent to manager"),
        APPROVED_BY_MANAGER("approved by manager"),
        REJECTED_BY_MANAGER_CAN_BE_FIXED("rejected by manager - can be fixed"),
        CLOSED_AS_REJECTED_BY_MANAGER("closed as rejected by manager"),
        APPROVED_BY_MANAGER_WAITING_FOR_INVOICE("approved by manager - waiting for invoice"),
        SENT_TO_ACCOUNTANT("sent to accountant"),
        CLOSED_AS_APPROVED("closed as approved"),
        REJECTED_BY_ACCOUNTANT_CAN_BE_FIXED("rejected by accountant - can be fixed"),
        CLOSED_AS_REJECTED_BY_ACCOUNTANT("closed as rejected by accountant"),
        CANCELED_BY_USER("canceled by user");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}