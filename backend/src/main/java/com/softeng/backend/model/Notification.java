package com.softeng.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String message;

    private String targetId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public enum NotificationType {
        ALL,
        ACCOUNTANT,
        DEPARTMENT,
        EMPLOYEE
    }

    public Notification() {
        this.createdAt = LocalDateTime.now();
    }

    public Notification(NotificationType type, String message, String targetId) {
        this();
        this.type = type;
        this.message = message;
        this.targetId = targetId;
    }
}