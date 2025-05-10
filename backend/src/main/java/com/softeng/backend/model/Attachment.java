package com.softeng.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "attachments")
@Data
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "ticketId", nullable = false)
    private Ticket ticket;

    @Lob
    @Column(nullable = false)
    private byte[] invoice;

    public Attachment() {
    }
}