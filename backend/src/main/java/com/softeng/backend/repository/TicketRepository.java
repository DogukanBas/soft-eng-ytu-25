package com.softeng.backend.repository;

import com.softeng.backend.dto.TicketDTOs;
import com.softeng.backend.dto.TicketSummary;
import com.softeng.backend.model.ApproveHistory;
import com.softeng.backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    List<Ticket> findByEmployeeId(String personalNo);
    List<Ticket> findByManagerId(String personalNo);
    @Query("""
SELECT DISTINCT new com.softeng.backend.dto.TicketSummary(
    ah.ticket.ticketId, ah.ticket.employeeId)
FROM ApproveHistory ah
WHERE ah.id = (
    SELECT MAX(subAh.id)
    FROM ApproveHistory subAh
    WHERE subAh.ticket = ah.ticket
)
AND (
    (:includeStatuses = true AND ah.status IN :statuses)
    OR (:includeStatuses = false AND ah.status NOT IN :statuses)
)
AND EXISTS (
    SELECT 1
    FROM ApproveHistory ah_actor_check
    WHERE ah_actor_check.ticket = ah.ticket
      AND (
        ah_actor_check.actor.personalNo = :personalNo
        OR (:isManager = true AND ah.ticket.managerId = :personalNo AND ah_actor_check.status = com.softeng.backend.model.ApproveHistory.Status.SENT_TO_MANAGER)
        OR (:isAccountant = true AND ah_actor_check.status = com.softeng.backend.model.ApproveHistory.Status.SENT_TO_ACCOUNTANT)
      )
)
""")
    List<TicketSummary> findAllTicketsByEmployeeId(
            @Param("personalNo") String personalNo,
            @Param("includeStatuses") boolean includeStatuses,
            @Param("statuses") List<ApproveHistory.Status> statuses,
            @Param("isManager") boolean isManager,
            @Param("isAccountant") boolean isAccountant
    );
    Optional<TicketDTOs.TicketWithoutInvoiceResponse> findByTicketId(Integer ticketId);
}

