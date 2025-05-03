package com.softeng.backend.repository;

import com.softeng.backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByEmployeeId(String personalNo);
    List<Ticket> findByManagerId(String personalNo);
}

