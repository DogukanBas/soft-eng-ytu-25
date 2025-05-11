package com.softeng.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

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
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private String description;

    @ManyToOne
    @JoinColumn(name = "actorId", nullable = false, referencedColumnName = "personalNo")
    private User actor;

    public ApproveHistory() {
    }

    public ApproveHistory(Ticket ticket, LocalDate date, Status status, String description, User actor) {
        this.ticket = ticket;
        this.date = date;
        this.status = status;
        this.description = description;
        this.actor = actor;
    }

    @Getter
    public enum Status {
        SENT_TO_MANAGER("SENT_TO_MANAGER"),
        REJECTED_BY_MANAGER_CAN_BE_FIXED("REJECTED_BY_MANAGER_CAN_BE_FIXED"),
        CLOSED_AS_REJECTED_BY_MANAGER("CLOSED_AS_REJECTED_BY_MANAGER"),
        SENT_TO_ACCOUNTANT("SENT_TO_ACCOUNTANT"),
        CLOSED_AS_APPROVED("CLOSED_AS_APPROVED"),
        REJECTED_BY_ACCOUNTANT_CAN_BE_FIXED("REJECTED_BY_ACCOUNTANT_CAN_BE_FIXED"),
        CLOSED_AS_REJECTED_BY_ACCOUNTANT("CLOSED_AS_REJECTED_BY_ACCOUNTANT"),
        CANCELED_BY_USER("CANCELED_BY_USER");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public static List<Status> getClosedStatus() {
            return List.of(
                    CLOSED_AS_APPROVED,
                    CLOSED_AS_REJECTED_BY_MANAGER,
                    CLOSED_AS_REJECTED_BY_ACCOUNTANT,
                    CANCELED_BY_USER
            );
        }

        public static List<Status> getTeamMemberCancelableStatus() {
            return List.of(
                    SENT_TO_MANAGER,
                    REJECTED_BY_MANAGER_CAN_BE_FIXED,
                    REJECTED_BY_ACCOUNTANT_CAN_BE_FIXED
            );
        }

        public static List<Status> getTeamMemberEditableStatus() {
            return List.of(
                    SENT_TO_MANAGER,
                    REJECTED_BY_MANAGER_CAN_BE_FIXED,
                    REJECTED_BY_ACCOUNTANT_CAN_BE_FIXED
            );
        }

        public static List<Status> getManagerCancelableStatus() {
            return List.of(
                    SENT_TO_ACCOUNTANT,
                    REJECTED_BY_ACCOUNTANT_CAN_BE_FIXED
            );
        }

        public static List<Status> getManagerEditableStatus() {
            return List.of(
                    SENT_TO_ACCOUNTANT,
                    REJECTED_BY_ACCOUNTANT_CAN_BE_FIXED
            );
        }

        public static List<Status> getAccountantEditableStatus() {
            return List.of(
                    SENT_TO_MANAGER
            );
        }


    }
}