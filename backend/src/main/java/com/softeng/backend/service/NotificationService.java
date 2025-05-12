package com.softeng.backend.service;

import com.softeng.backend.model.Notification;
import java.util.List;

public interface NotificationService {
    void createNotification(Notification.NotificationType type, String message, String targetId);
    List<Notification> getUserNotifications(String personalNo, boolean isAccountant);
}