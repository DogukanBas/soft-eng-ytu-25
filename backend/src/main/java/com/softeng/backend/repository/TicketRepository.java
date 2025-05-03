package com.softeng.backend.repository;

import com.softeng.backend.dto.TicketDTOs;
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
        SELECT DISTINCT ah.ticket.ticketId
        FROM ApproveHistory ah
        WHERE ah.status IN (
            com.softeng.backend.model.ApproveHistory$Status.CLOSED_AS_REJECTED_BY_MANAGER,
            com.softeng.backend.model.ApproveHistory$Status.CLOSED_AS_APPROVED,
            com.softeng.backend.model.ApproveHistory$Status.CLOSED_AS_REJECTED_BY_ACCOUNTANT,
            com.softeng.backend.model.ApproveHistory$Status.CANCELED_BY_USER
          )
          AND EXISTS (
              SELECT 1
              FROM ApproveHistory ah_actor_check
              WHERE ah_actor_check.ticket = ah.ticket
                AND ah_actor_check.actor.personalNo = :personalNo
          )
        """)
    List<Integer> findAllClosedTicketsByEmployeeId(@Param("personalNo") String personalNo);
    Optional<TicketDTOs.TicketWithoutInvoiceResponse> findByTicketId(Integer ticketId);
}

