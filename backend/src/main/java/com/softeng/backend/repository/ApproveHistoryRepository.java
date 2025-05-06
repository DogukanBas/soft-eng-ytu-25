package com.softeng.backend.repository;

import com.softeng.backend.model.ApproveHistory;
import com.softeng.backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApproveHistoryRepository extends JpaRepository<ApproveHistory, Integer> {
    List<ApproveHistory> findByTicketOrderByDateDesc(Ticket ticket);
    List<ApproveHistory> findByTicket(Ticket ticket);
    Optional<ApproveHistory> findFirstByTicketOrderByDateDesc(Ticket ticket);
}