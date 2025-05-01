package com.softeng.backend.repository;

import com.softeng.backend.model.Ticket;
import com.softeng.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByEmployeeId(String personalNo);
    List<Ticket> findByManagerId(String personalNo);
}

