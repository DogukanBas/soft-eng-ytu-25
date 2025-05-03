package com.softeng.backend.service.impl;

import com.softeng.backend.dto.TicketDTOs;
import com.softeng.backend.exception.ResourceNotFoundException;
import com.softeng.backend.model.ApproveHistory;
import com.softeng.backend.model.Attachment;
import com.softeng.backend.model.Employee;
import com.softeng.backend.model.Ticket;
import com.softeng.backend.repository.ApproveHistoryRepository;
import com.softeng.backend.repository.AttachmentRepository;
import com.softeng.backend.repository.EmployeeRepository;
import com.softeng.backend.repository.TicketRepository;
import com.softeng.backend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final AttachmentRepository attachmentRepository;
    private final ApproveHistoryRepository approveHistoryRepository;
    private final EmployeeRepository employeeRepository;
    @Autowired
    public TicketServiceImpl(
            TicketRepository ticketRepository,
            AttachmentRepository attachmentRepository,
            ApproveHistoryRepository approveHistoryRepository,
            EmployeeRepository employeeRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.attachmentRepository = attachmentRepository;
        this.approveHistoryRepository = approveHistoryRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Ticket> getTicketByEmployeeId(String personalNo) {
        return ticketRepository.findByEmployeeId(personalNo);
    }

    @Override
    public List<Ticket> getTicketByManagerId(String personalNo) {
        return ticketRepository.findByManagerId(personalNo);
    }

    @Override
    @Transactional
    public void addAttachment(Attachment attachment) {
        try {
            attachmentRepository.save(attachment);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error adding attachment: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void addApproveHistory(ApproveHistory approveHistory) {
        try {
            approveHistoryRepository.save(approveHistory);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error adding employee: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void addTicket(Ticket ticket) {
        try {
            ticketRepository.save(ticket);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error adding ticket: " + e.getMessage());
        }
    }
    @Override
    public List<Integer> getClosedTicketIdsByEmployeeId(String personalNo) {
        return ticketRepository.findAllClosedTicketsByEmployeeId(personalNo);
    }

    @Override
    public TicketDTOs.TicketWithoutInvoiceResponse getTicketById(Integer ticketId) {
        return ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ticket id : " + ticketId));
    }



}
