package com.softeng.backend.service.impl;

import com.softeng.backend.exception.ResourceNotFoundException;
import com.softeng.backend.model.Employee;
import com.softeng.backend.model.Notification;
import com.softeng.backend.repository.NotificationRepository;
import com.softeng.backend.service.EmployeeService;
import com.softeng.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmployeeService employeeService;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                 EmployeeService employeeService) {
        this.notificationRepository = notificationRepository;
        this.employeeService = employeeService;
    }

    @Override
    @Transactional
    public void createNotification(Notification.NotificationType type, String message, String targetId) {
        Notification notification = new Notification(type, message, targetId);
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(String personalNo, boolean isAccountant) {
        Employee employee = employeeService.getEmployeeByPersonalNo(personalNo);
        String departmentId = employee.getDepartment().getDeptId().toString();
        
        return notificationRepository.findUserNotifications(departmentId, personalNo, isAccountant);
    }
}