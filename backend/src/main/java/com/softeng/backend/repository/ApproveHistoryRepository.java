package com.softeng.backend.repository;

import com.softeng.backend.dto.AccountantDTOs;
import com.softeng.backend.model.ApproveHistory;
import com.softeng.backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApproveHistoryRepository extends JpaRepository<ApproveHistory, Integer> {
    List<ApproveHistory> findByTicketOrderByDateDesc(Ticket ticket);
    List<ApproveHistory> findByTicket(Ticket ticket);
    Optional<ApproveHistory> findFirstByTicketOrderByIdDesc(Ticket ticket);

    @Query("SELECT NEW com.softeng.backend.dto.AccountantDTOs$StatEntry(" +
            "CAST(ah.date AS string), " +
            "SUM(CAST(ah.ticket.amount AS double))) " +
            "FROM ApproveHistory ah " +
            "JOIN Employee e ON ah.ticket.employeeId = e.personalNo " +
            "WHERE e.department.deptId = :deptId " +
            "AND ah.status = com.softeng.backend.model.ApproveHistory.Status.CLOSED_AS_APPROVED " +
            "GROUP BY ah.date")
    List<AccountantDTOs.StatEntry> findApprovedByDepartmentId(@Param("deptId") Integer deptId);

    @Query("SELECT NEW com.softeng.backend.dto.AccountantDTOs$StatEntry(" +
           "CAST(ah.date AS string), " +
           "SUM(CAST(ah.ticket.amount AS double))) " +
           "FROM ApproveHistory ah " +
           "WHERE ah.ticket.employeeId = :personalNo " +
           "AND ah.status = com.softeng.backend.model.ApproveHistory.Status.CLOSED_AS_APPROVED " +
           "GROUP BY ah.date")
    List<AccountantDTOs.StatEntry> findApprovedByEmployeeId(@Param("personalNo") String personalNo);

    @Query("SELECT NEW com.softeng.backend.dto.AccountantDTOs$StatEntry(" +
           "CAST(ah.date AS string), " +
           "SUM(CAST(ah.ticket.amount AS double))) " +
           "FROM ApproveHistory ah " +
           "WHERE ah.ticket.costType = :costType " +
           "AND ah.status = com.softeng.backend.model.ApproveHistory.Status.CLOSED_AS_APPROVED " +
           "GROUP BY ah.date")
    List<AccountantDTOs.StatEntry> findApprovedByCostType(@Param("costType") String costType);
}