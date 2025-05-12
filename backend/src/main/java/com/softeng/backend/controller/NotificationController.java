package com.softeng.backend.controller;

import com.softeng.backend.dto.NotificationDTOs;
import com.softeng.backend.model.Notification;
import com.softeng.backend.model.User;
import com.softeng.backend.service.NotificationService;
import com.softeng.backend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@SecurityRequirement(name = "BearerAuth")
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNotification(@RequestBody NotificationDTOs.CreateNotificationRequest request,
                                              Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", NotificationDTOs.NotificationResponseStatus.INVALID_AUTHENTICATION.getMessage()));
        }

        notificationService.createNotification(request.getType(), request.getMessage(), request.getTargetId());
        return ResponseEntity.ok(Map.of("message", 
            NotificationDTOs.NotificationResponseStatus.NOTIFICATION_CREATED.getMessage()));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserNotifications(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", NotificationDTOs.NotificationResponseStatus.INVALID_AUTHENTICATION.getMessage()));
        }

        String personalNo = authentication.getName();
        boolean isAccountant = userService.getUserByPersonalNo(personalNo).getUserType() == User.UserType.accountant;
        List<NotificationDTOs.NotificationResponse> notifications = notificationService
            .getUserNotifications(authentication.getName(), isAccountant)
            .stream()
            .map(NotificationDTOs.NotificationResponse::new)
            .collect(Collectors.toList());

        return ResponseEntity.ok(notifications);
    }
}