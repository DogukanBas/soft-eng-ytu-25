package com.softeng.backend.repository;

import com.softeng.backend.model.Attachment;
import com.softeng.backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    Optional<Attachment> findByTicket(Ticket ticket);
}