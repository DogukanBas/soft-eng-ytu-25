package com.softeng.backend.service.impl;

import com.softeng.backend.dto.TicketDTOs;
import com.softeng.backend.dto.TicketSummary;
import com.softeng.backend.exception.ResourceNotFoundException;
import com.softeng.backend.model.ApproveHistory;
import com.softeng.backend.model.Attachment;
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
    public List<Integer> getCreatedClosedTicketIdsByPersonalNo(String personalNo, boolean isManager, boolean isAccountant) {
        List<TicketSummary> allTickets = ticketRepository.findAllTicketsByEmployeeId(personalNo, true,ApproveHistory.Status.getClosedStatus(),isManager,isAccountant);
        return allTickets.stream()
                .filter(ticket -> ticket.employeeId().equals(personalNo))
                .map(TicketSummary::ticketId)
                .toList();
    }

    @Override
    public List<Integer> getCreatedActiveTicketIdsByPersonalNo(String personalNo, boolean isManager, boolean isAccountant) {
        List<TicketSummary> allTickets = ticketRepository.findAllTicketsByEmployeeId(personalNo, false,ApproveHistory.Status.getClosedStatus(), isManager,isAccountant);
        return allTickets.stream()
                .filter(ticket -> ticket.employeeId().equals(personalNo))
                .map(TicketSummary::ticketId)
                .toList();
    }

    @Override
    public List<Integer> getAssignedClosedTicketIdsByPersonalNo(String personalNo, boolean isManager, boolean isAccountant) {
        List<TicketSummary> allTickets = ticketRepository.findAllTicketsByEmployeeId(personalNo, true,ApproveHistory.Status.getClosedStatus(), isManager,isAccountant);
        return allTickets.stream()
                .filter(ticket -> !ticket.employeeId().equals(personalNo))
                .map(TicketSummary::ticketId)
                .toList();
    }

    @Override
    public List<Integer> getAssignedActiveTicketIdsByPersonalNo(String personalNo, boolean isManager, boolean isAccountant) {
        List<TicketSummary> allTickets = ticketRepository.findAllTicketsByEmployeeId(personalNo, false,ApproveHistory.Status.getClosedStatus(), isManager,isAccountant);

        return allTickets.stream()
                .filter(ticket -> !ticket.employeeId().equals(personalNo))
                .map(TicketSummary::ticketId)
                .toList();
    }

    @Override
    public TicketDTOs.TicketWithoutInvoiceResponse getTicketById(Integer ticketId) {
        return ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ticket id : " + ticketId));
    }

    @Override
    public List<TicketDTOs.ApproveHistoryResponse> getApproveHistoryByTicketId(Integer ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ticket id : " + ticketId));
        return approveHistoryRepository.findByTicket(ticket).stream().toList()
                .stream()
                .map(TicketDTOs.ApproveHistoryResponse::new)
                .toList();
    }

    @Override
    public ApproveHistory getLastApproveHistoryByTicketId(Integer ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ticket id : " + ticketId));
        return approveHistoryRepository.findFirstByTicketOrderByIdDesc(ticket)
                .orElseThrow(() -> new ResourceNotFoundException("Approve history not found for ticket id: " + ticketId));
    }

    @Override
    public Attachment getAttachmentByTicketId(Integer ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with ticket id : " + ticketId));
        return attachmentRepository.findByTicket(ticket)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found for ticket id: " + ticketId));
    }
}
