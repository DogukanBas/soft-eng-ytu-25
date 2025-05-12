package com.softeng.backend.repository;

import com.softeng.backend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query("SELECT n " +
           "FROM Notification n " +
           "WHERE n.type = 'ALL' OR " +
           "(n.type = 'DEPARTMENT' AND n.targetId = :departmentId) OR " +
           "(n.type = 'EMPLOYEE' AND n.targetId = :employeeId) OR " +
           "(n.type = 'ACCOUNTANT' AND :isAccountant = true) " +
           "ORDER BY n.id DESC")
    List<Notification> findUserNotifications(
            @Param("departmentId") String departmentId,
            @Param("employeeId") String employeeId,
            @Param("isAccountant") boolean isAccountant);
}