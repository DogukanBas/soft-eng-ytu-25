package com.softeng.backend.dto;

import com.softeng.backend.model.Notification;
import java.time.LocalDate;

public class NotificationDTOs {
    public static class CreateNotificationRequest {
        private Notification.NotificationType type;
        private String message;
        private String targetId;

        public Notification.NotificationType getType() { return type; }
        public void setType(Notification.NotificationType type) { this.type = type; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
    }

    public static class NotificationResponse {
        private Integer id;
        private String type;
        private String message;
        private LocalDate createdAt;

        public NotificationResponse(Notification notification) {
            this.id = notification.getId();
            this.type = notification.getType().name();
            this.message = notification.getMessage();
            this.createdAt = notification.getCreatedAt().toLocalDate();
        }

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public LocalDate getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    }

    public enum NotificationResponseStatus {
        NOTIFICATION_CREATED("Notification created successfully"),
        INVALID_AUTHENTICATION("Invalid authentication");

        private final String message;

        NotificationResponseStatus(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}