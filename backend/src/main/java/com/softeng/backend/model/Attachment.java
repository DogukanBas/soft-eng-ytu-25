package com.softeng.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "attachments")
@Data
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticketId", nullable = false)
    private Ticket ticket;

    @Lob
    @Column(columnDefinition = "BYTEA", nullable = false)
    private byte[] invoice;
}